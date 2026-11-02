package com.route.firstapp.ui.productsCatalog

import com.route.domain.data.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ProductsCatalogContract {
    abstract class ViewModel : androidx.lifecycle.ViewModel(){
        abstract val states : StateFlow<State>
        abstract val events : Flow<Event>
        abstract fun doIntent(intent: Intent)
    }

    sealed class State{
        data class Success(
            val products: List<Product>
        ): State()
        data class Error(
            val message: String,
        ): State()
        data object Loading: State()
    }
    sealed class Intent{
        data class Init(
            val categoryId: String,
            val subCategoryId: String,
        ): Intent()
        data class ProductClicked(val product: Product): Intent()
    }
    sealed class Event{
        data class NavigateToProductDetails(val productId: String): Event()
    }
}
