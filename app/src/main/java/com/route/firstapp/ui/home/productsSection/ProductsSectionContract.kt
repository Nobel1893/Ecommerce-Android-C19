package com.route.firstapp.ui.home.productsSection

import com.route.domain.data.Product
import com.route.domain.data.ProductsSort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ProductsSectionContract {
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
            val message:String,
        ): State()
        data object Loading: State()
    }
    sealed class Intent{
        data class Init(val sort: ProductsSort): Intent()

    }
    sealed class Event{
    }
}