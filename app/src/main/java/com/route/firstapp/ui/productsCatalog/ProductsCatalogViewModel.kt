package com.route.firstapp.ui.productsCatalog

import androidx.lifecycle.viewModelScope
import com.route.domain.data.Product
import com.route.domain.data.Resource
import com.route.domain.usecase.GetSubCategoryProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsCatalogViewModel @Inject constructor(
    private val getSubCategoryProductsUseCase: GetSubCategoryProductsUseCase,
) : ProductsCatalogContract.ViewModel(){

    private val _events = Channel<ProductsCatalogContract.Event>(Channel.BUFFERED)
    override val events: Flow<ProductsCatalogContract.Event> = _events.receiveAsFlow()

    private val _states = MutableStateFlow<ProductsCatalogContract.State>(ProductsCatalogContract.State.Loading)
    override val states: StateFlow<ProductsCatalogContract.State> = _states.asStateFlow()

    private var productsJob: Job? = null

    override fun doIntent(intent: ProductsCatalogContract.Intent) {
        when (intent) {
            is ProductsCatalogContract.Intent.Init -> {
                // Init is sent again when the user comes back from product details, keep the loaded products
                val isLoadedOrLoading = _states.value is ProductsCatalogContract.State.Success ||
                        productsJob?.isActive == true
                if (!isLoadedOrLoading) loadProducts(
                    categoryId = intent.categoryId,
                    subCategoryId = intent.subCategoryId,
                )
            }
            is ProductsCatalogContract.Intent.ProductClicked -> handleProductClick(product = intent.product)
        }
    }

    private fun loadProducts(categoryId: String, subCategoryId: String){
        productsJob = viewModelScope.launch {
            getSubCategoryProductsUseCase.getSubCategoryProducts(
                categoryId = categoryId,
                subCategoryId = subCategoryId,
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _states.emit(ProductsCatalogContract.State.Loading)
                    }
                    is Resource.Error -> {
                        _states.emit(
                            ProductsCatalogContract.State.Error(message = resource.throwable.message ?: "")
                        )
                    }
                    is Resource.Success -> {
                        _states.emit(
                            ProductsCatalogContract.State.Success(products = resource.data ?: listOf())
                        )
                    }
                }
            }
        }
    }

    private fun handleProductClick(product: Product){
        viewModelScope.launch {
            _events.send(ProductsCatalogContract.Event.NavigateToProductDetails(productId = product.id))
        }
    }
}
