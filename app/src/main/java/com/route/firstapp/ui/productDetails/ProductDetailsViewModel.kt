package com.route.firstapp.ui.productDetails

import androidx.lifecycle.viewModelScope
import com.route.domain.data.Product
import com.route.domain.data.Resource
import com.route.domain.usecase.GetProductDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
) : ProductDetailsContract.ViewModel(){

    private val _events = Channel<ProductDetailsContract.Event>(Channel.BUFFERED)
    override val events: Flow<ProductDetailsContract.Event> = _events.receiveAsFlow()

    private val _states = MutableStateFlow<ProductDetailsContract.State>(ProductDetailsContract.State.Loading)
    override val states: StateFlow<ProductDetailsContract.State> = _states.asStateFlow()

    private var productJob: Job? = null

    override fun doIntent(intent: ProductDetailsContract.Intent) {
        when (intent) {
            is ProductDetailsContract.Intent.Init -> {
                // Init is sent again on rotation, keep the loaded product and the chosen quantity
                val isLoadedOrLoading = _states.value is ProductDetailsContract.State.Success ||
                        productJob?.isActive == true
                if (!isLoadedOrLoading) loadProduct(productId = intent.productId)
            }
            ProductDetailsContract.Intent.IncreaseQuantityClicked -> changeQuantity(by = 1)
            ProductDetailsContract.Intent.DecreaseQuantityClicked -> changeQuantity(by = -1)
            ProductDetailsContract.Intent.AddToCartClicked -> sendEvent(ProductDetailsContract.Event.ShowCartComingSoon)
            ProductDetailsContract.Intent.BackClicked -> sendEvent(ProductDetailsContract.Event.NavigateBack)
        }
    }

    private fun loadProduct(productId: String){
        productJob = viewModelScope.launch {
            getProductDetailsUseCase.getProductDetails(productId = productId)
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _states.emit(ProductDetailsContract.State.Loading)
                        }
                        is Resource.Error -> {
                            _states.emit(
                                ProductDetailsContract.State.Error(message = resource.throwable.message ?: "")
                            )
                        }
                        is Resource.Success -> {
                            val product = resource.data
                            _states.emit(
                                if (product == null) ProductDetailsContract.State.Error(message = "Product not found")
                                else successState(product = product, quantity = MIN_QUANTITY)
                            )
                        }
                    }
                }
        }
    }

    private fun changeQuantity(by: Int){
        _states.update { current ->
            if (current !is ProductDetailsContract.State.Success) return@update current
            // can't order more than is in stock
            val maxQuantity = (current.product.availableQuantity ?: Int.MAX_VALUE).coerceAtLeast(MIN_QUANTITY)
            successState(
                product = current.product,
                quantity = (current.quantity + by).coerceIn(MIN_QUANTITY, maxQuantity),
            )
        }
    }

    private fun successState(product: Product, quantity: Int): ProductDetailsContract.State.Success {
        return ProductDetailsContract.State.Success(
            product = product,
            quantity = quantity,
            totalPrice = product.finalPrice?.times(quantity),
        )
    }

    private fun sendEvent(event: ProductDetailsContract.Event){
        viewModelScope.launch {
            _events.send(event)
        }
    }

    private companion object {
        const val MIN_QUANTITY = 1
    }
}
