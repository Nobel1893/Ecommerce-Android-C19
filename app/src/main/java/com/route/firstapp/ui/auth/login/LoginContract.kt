package com.route.firstapp.ui.auth.login

import com.route.domain.data.FieldError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LoginContract {
    abstract class ViewModel : androidx.lifecycle.ViewModel(){
        abstract val states : StateFlow<State>
        abstract val events : Flow<Event>
        abstract fun doIntent(intent: Intent)
    }

    // A form keeps what the user typed through validation, loading and server errors,
    // so its state is one immutable snapshot rather than Loading / Success / Error.
    data class State(
        // the design labels this field "User Name", but the API signs in with an email
        val email: String = "",
        val password: String = "",
        val emailError: FieldError? = null,
        val passwordError: FieldError? = null,
        val isPasswordVisible: Boolean = false,
        val isLoading: Boolean = false,
    )

    sealed class Intent{
        data class EmailChanged(val email: String): Intent()
        data class PasswordChanged(val password: String): Intent()
        data object PasswordVisibilityToggled: Intent()
        data object LoginClicked: Intent()
        data object CreateAccountClicked: Intent()
    }

    sealed class Event{
        data object NavigateToHome: Event()
        data object NavigateToRegister: Event()
        // the server's message ("Incorrect email or password"); null shows the generic error
        data class ShowError(val message: String?): Event()
    }
}
