package com.route.firstapp.ui.auth.register

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.route.firstapp.R
import com.route.firstapp.ui.AppRoutes
import kotlinx.coroutines.launch

// Entry composable only. It has no preview because it needs a Hilt ViewModel;
// RegisterContent and its previews cover the UI. Back to Login is the system back.
@Composable
fun RegisterScreen(modifier: Modifier = Modifier,
                   viewModel: RegisterViewModel = hiltViewModel(),
                   onNavigate: (AppRoutes) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val genericErrorMessage = stringResource(R.string.generic_error_message)
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                RegisterContract.Event.NavigateToHome -> onNavigate(AppRoutes.Home)
                // in its own coroutine: showSnackbar suspends until the snackbar is dismissed
                is RegisterContract.Event.ShowError -> launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(event.message ?: genericErrorMessage)
                }
            }
        }
    }
    val states = viewModel.states.collectAsStateWithLifecycle()

    RegisterContent(
        modifier = modifier,
        state = states.value,
        snackbarHostState = snackbarHostState,
        onNameChange = { name -> viewModel.doIntent(RegisterContract.Intent.NameChanged(name)) },
        onPhoneChange = { phone -> viewModel.doIntent(RegisterContract.Intent.PhoneChanged(phone)) },
        onEmailChange = { email -> viewModel.doIntent(RegisterContract.Intent.EmailChanged(email)) },
        onPasswordChange = { password -> viewModel.doIntent(RegisterContract.Intent.PasswordChanged(password)) },
        onPasswordVisibilityToggle = { viewModel.doIntent(RegisterContract.Intent.PasswordVisibilityToggled) },
        onSignUpClick = { viewModel.doIntent(RegisterContract.Intent.SignUpClicked) },
    )
}
