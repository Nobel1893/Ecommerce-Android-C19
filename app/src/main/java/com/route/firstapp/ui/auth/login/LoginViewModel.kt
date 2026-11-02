package com.route.firstapp.ui.auth.login

import androidx.lifecycle.viewModelScope
import com.route.domain.data.AuthSession
import com.route.domain.data.Resource
import com.route.domain.data.ServerException
import com.route.domain.usecase.LoginUseCase
import com.route.domain.usecase.ValidateLoginFormUseCase
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
class LoginViewModel @Inject constructor(
    private val validateLoginFormUseCase: ValidateLoginFormUseCase,
    private val loginUseCase: LoginUseCase,
) : LoginContract.ViewModel(){

    private val _events = Channel<LoginContract.Event>(Channel.BUFFERED)
    override val events: Flow<LoginContract.Event> = _events.receiveAsFlow()

    private val _states = MutableStateFlow(LoginContract.State())
    override val states: StateFlow<LoginContract.State> = _states.asStateFlow()

    private var loginJob: Job? = null

    override fun doIntent(intent: LoginContract.Intent) {
        when (intent) {
            is LoginContract.Intent.EmailChanged -> updateEmail(email = intent.email)
            is LoginContract.Intent.PasswordChanged -> updatePassword(password = intent.password)
            LoginContract.Intent.PasswordVisibilityToggled -> togglePasswordVisibility()
            LoginContract.Intent.LoginClicked -> submitLogin()
            LoginContract.Intent.CreateAccountClicked -> sendEvent(LoginContract.Event.NavigateToRegister)
        }
    }

    // editing a field clears its error until the next submit
    private fun updateEmail(email: String){
        _states.update { state -> state.copy(email = email, emailError = null) }
    }

    private fun updatePassword(password: String){
        _states.update { state -> state.copy(password = password, passwordError = null) }
    }

    private fun togglePasswordVisibility(){
        _states.update { state -> state.copy(isPasswordVisible = !state.isPasswordVisible) }
    }

    private fun submitLogin(){
        if (loginJob?.isActive == true) return
        val state = _states.value
        val errors = validateLoginFormUseCase.validateLoginForm(
            email = state.email,
            password = state.password,
        )
        if (!errors.isValid) {
            _states.update { current ->
                current.copy(emailError = errors.email, passwordError = errors.password)
            }
            return
        }
        login(email = state.email, password = state.password)
    }

    private fun login(email: String, password: String){
        loginJob = viewModelScope.launch {
            loginUseCase.login(email = email, password = password)
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> _states.update { state -> state.copy(isLoading = true) }
                        is Resource.Success -> handleLoginSuccess(session = resource.data)
                        is Resource.Error -> handleLoginError(throwable = resource.throwable)
                    }
                }
        }
    }

    private fun handleLoginSuccess(session: AuthSession?){
        if (session == null) {
            handleLoginError(throwable = null)
            return
        }
        // isLoading stays on: the form must not be submitted again while Home opens
        sendEvent(LoginContract.Event.NavigateToHome)
    }

    private fun handleLoginError(throwable: Throwable?){
        _states.update { state -> state.copy(isLoading = false) }
        sendEvent(
            LoginContract.Event.ShowError(message = (throwable as? ServerException)?.serverMessage)
        )
    }

    private fun sendEvent(event: LoginContract.Event){
        viewModelScope.launch {
            _events.send(event)
        }
    }
}
