package com.route.firstapp.ui.auth.register

import com.route.domain.data.FieldError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RegisterContract {
    abstract class ViewModel : androidx.lifecycle.ViewModel(){
        abstract val states : StateFlow<State>
        abstract val events : Flow<Event>
        abstract fun doIntent(intent: Intent)
    }

    // A form keeps what the user typed through validation, loading and server errors,
    // so its state is one immutable snapshot rather than Loading / Success / Error.
    data class State(
        val name: String = "",
        val phone: String = "",
        val email: String = "",
        val password: String = "",
        val nameError: FieldError? = null,
        val phoneError: FieldError? = null,
        val emailError: FieldError? = null,
        val passwordError: FieldError? = null,
        val isPasswordVisible: Boolean = false,
        val isLoading: Boolean = false,
    )

    sealed class Intent{
        data class NameChanged(val name: String): Intent()
        data class PhoneChanged(val phone: String): Intent()
        data class EmailChanged(val email: String): Intent()
        data class PasswordChanged(val password: String): Intent()
        data object PasswordVisibilityToggled: Intent()
        data object SignUpClicked: Intent()
    }

    sealed class Event{
        data object NavigateToHome: Event()
        // the server's message ("Account Already Exists"); null shows the generic error
        data class ShowError(val message: String?): Event()
    }
}
