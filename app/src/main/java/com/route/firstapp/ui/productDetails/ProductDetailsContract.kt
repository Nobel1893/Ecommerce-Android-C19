package com.route.firstapp.ui.productDetails

import com.route.domain.data.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ProductDetailsContract {
    abstract class ViewModel : androidx.lifecycle.ViewModel(){
        abstract val states : StateFlow<State>
        abstract val events : Flow<Event>
        abstract fun doIntent(intent: Intent)
    }

    sealed class State{
        data class Success(
            val product: Product,
            val quantity: Int,
            // null when the API sent no price
            val totalPrice: Int?,
        ): State()
        data class Error(
            val message: String,
        ): State()
        data object Loading: State()
    }
    sealed class Intent{
        data class Init(val productId: String): Intent()
        data object IncreaseQuantityClicked: Intent()
        data object DecreaseQuantityClicked: Intent()
        data object AddToCartClicked: Intent()
        data object BackClicked: Intent()
    }
    sealed class Event{
        data object NavigateBack: Event()
        // the cart API needs a sign-in token, which the app doesn't have yet
        data object ShowCartComingSoon: Event()
    }
}
