package com.route.firstapp.ui.home.productsSection

import androidx.lifecycle.viewModelScope
import com.route.domain.data.Product
import com.route.domain.data.ProductsSort
import com.route.domain.data.Resource
import com.route.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    val getProductsUseCase : GetProductsUseCase,
) : ProductsSectionContract.ViewModel(){

    private val _events = Channel<ProductsSectionContract.Event>(Channel.BUFFERED)
    override val events: Flow<ProductsSectionContract.Event>
        get() = _events.receiveAsFlow()

    private val _states = MutableStateFlow<ProductsSectionContract.State>(ProductsSectionContract.State.Loading)
    override val states: StateFlow<ProductsSectionContract.State>
        get() = _states.asStateFlow()

    override fun doIntent(intent: ProductsSectionContract.Intent) {
        when (intent) {
            is ProductsSectionContract.Intent.Init -> loadProducts(
                sort = intent.sort,
            )
            is ProductsSectionContract.Intent.ProductClicked -> {
                handleProductClick(product = intent.product)
            }
        }
    }
    private fun handleProductClick(product: Product){
        // track product Clicked
        product.id?.let { productId ->
            viewModelScope.launch {
                _events.send(
                    ProductsSectionContract.Event.NavigateToProductDetails(
                        productId = product.id
                    )
                )
            }
        }
    }

    private fun loadProducts(
        sort: ProductsSort
    ){
        viewModelScope.launch {
            getProductsUseCase.getSortedProducts(sort = sort)
                .collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            _states.emit(ProductsSectionContract.State.Error("" +
                                    "Something Went Wrong"))
                        }
                        is Resource.Loading -> {
                            _states.emit(
                                ProductsSectionContract.State.Loading
                            )
                        }
                        is Resource.Success -> {
                            _states.emit(
                                ProductsSectionContract.State.Success(
                                    products = resource.data ?: listOf()
                                )
                            )
                        }
                    }
                }
        }
    }
}