package com.route.firstapp.ui.auth.login

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
// LoginContent and its previews cover the UI.
@Composable
fun LoginScreen(modifier: Modifier = Modifier,
                viewModel: LoginViewModel = hiltViewModel(),
                onNavigate: (AppRoutes) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val genericErrorMessage = stringResource(R.string.generic_error_message)
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                LoginContract.Event.NavigateToHome -> onNavigate(AppRoutes.Home)
                LoginContract.Event.NavigateToRegister -> onNavigate(AppRoutes.Register)
                // in its own coroutine: showSnackbar suspends until the snackbar is dismissed
                is LoginContract.Event.ShowError -> launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(event.message ?: genericErrorMessage)
                }
            }
        }
    }
    val states = viewModel.states.collectAsStateWithLifecycle()

    LoginContent(
        modifier = modifier,
        state = states.value,
        snackbarHostState = snackbarHostState,
        onEmailChange = { email -> viewModel.doIntent(LoginContract.Intent.EmailChanged(email)) },
        onPasswordChange = { password -> viewModel.doIntent(LoginContract.Intent.PasswordChanged(password)) },
        onPasswordVisibilityToggle = { viewModel.doIntent(LoginContract.Intent.PasswordVisibilityToggled) },
        onLoginClick = { viewModel.doIntent(LoginContract.Intent.LoginClicked) },
        onCreateAccountClick = { viewModel.doIntent(LoginContract.Intent.CreateAccountClicked) },
    )
}
