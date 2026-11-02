package com.route.firstapp.ui.auth.register

import androidx.lifecycle.viewModelScope
import com.route.domain.data.AuthSession
import com.route.domain.data.RegisterFormErrors
import com.route.domain.data.RegistrationDetails
import com.route.domain.data.Resource
import com.route.domain.data.ServerException
import com.route.domain.usecase.RegisterUseCase
import com.route.domain.usecase.ValidateRegisterFormUseCase
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
class RegisterViewModel @Inject constructor(
    private val validateRegisterFormUseCase: ValidateRegisterFormUseCase,
    private val registerUseCase: RegisterUseCase,
) : RegisterContract.ViewModel(){

    private val _events = Channel<RegisterContract.Event>(Channel.BUFFERED)
    override val events: Flow<RegisterContract.Event> = _events.receiveAsFlow()

    private val _states = MutableStateFlow(RegisterContract.State())
    override val states: StateFlow<RegisterContract.State> = _states.asStateFlow()

    private var registerJob: Job? = null

    override fun doIntent(intent: RegisterContract.Intent) {
        when (intent) {
            is RegisterContract.Intent.NameChanged -> updateName(name = intent.name)
            is RegisterContract.Intent.PhoneChanged -> updatePhone(phone = intent.phone)
            is RegisterContract.Intent.EmailChanged -> updateEmail(email = intent.email)
            is RegisterContract.Intent.PasswordChanged -> updatePassword(password = intent.password)
            RegisterContract.Intent.PasswordVisibilityToggled -> togglePasswordVisibility()
            RegisterContract.Intent.SignUpClicked -> submitRegistration()
        }
    }

    // editing a field clears its error until the next submit
    private fun updateName(name: String){
        _states.update { state -> state.copy(name = name, nameError = null) }
    }

    private fun updatePhone(phone: String){
        _states.update { state -> state.copy(phone = phone, phoneError = null) }
    }

    private fun updateEmail(email: String){
        _states.update { state -> state.copy(email = email, emailError = null) }
    }

    private fun updatePassword(password: String){
        _states.update { state -> state.copy(password = password, passwordError = null) }
    }

    private fun togglePasswordVisibility(){
        _states.update { state -> state.copy(isPasswordVisible = !state.isPasswordVisible) }
    }

    private fun submitRegistration(){
        if (registerJob?.isActive == true) return
        val state = _states.value
        val details = RegistrationDetails(
            name = state.name,
            phone = state.phone,
            email = state.email,
            password = state.password,
        )
        val errors = validateRegisterFormUseCase.validateRegisterForm(details = details)
        if (!errors.isValid) {
            showFieldErrors(errors = errors)
            return
        }
        register(details = details)
    }

    private fun showFieldErrors(errors: RegisterFormErrors){
        _states.update { state ->
            state.copy(
                nameError = errors.name,
                phoneError = errors.phone,
                emailError = errors.email,
                passwordError = errors.password,
            )
        }
    }

    private fun register(details: RegistrationDetails){
        registerJob = viewModelScope.launch {
            registerUseCase.register(details = details)
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> _states.update { state -> state.copy(isLoading = true) }
                        is Resource.Success -> handleRegisterSuccess(session = resource.data)
                        is Resource.Error -> handleRegisterError(throwable = resource.throwable)
                    }
                }
        }
    }

    private fun handleRegisterSuccess(session: AuthSession?){
        if (session == null) {
            handleRegisterError(throwable = null)
            return
        }
        // isLoading stays on: the form must not be submitted again while Home opens
        sendEvent(RegisterContract.Event.NavigateToHome)
    }

    private fun handleRegisterError(throwable: Throwable?){
        _states.update { state -> state.copy(isLoading = false) }
        sendEvent(
            RegisterContract.Event.ShowError(message = (throwable as? ServerException)?.serverMessage)
        )
    }

    private fun sendEvent(event: RegisterContract.Event){
        viewModelScope.launch {
            _events.send(event)
        }
    }
}
