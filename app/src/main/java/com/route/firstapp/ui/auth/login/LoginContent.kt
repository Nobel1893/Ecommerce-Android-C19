package com.route.firstapp.ui.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.route.domain.data.FieldError
import com.route.firstapp.R
import com.route.firstapp.ui.auth.AuthLabeledField
import com.route.firstapp.ui.auth.AuthLogo
import com.route.firstapp.ui.auth.AuthPasswordField
import com.route.firstapp.ui.auth.AuthPrimaryButton
import com.route.firstapp.ui.auth.AuthScreenContainer
import com.route.firstapp.ui.auth.AuthTextField
import com.route.firstapp.ui.auth.fieldErrorMessage
import com.route.firstapp.ui.theme.AuthLinkTextStyle
import com.route.firstapp.ui.theme.AuthPromptTextStyle
import com.route.firstapp.ui.theme.AuthSubtitleTextStyle
import com.route.firstapp.ui.theme.AuthTitleTextStyle
import com.route.firstapp.ui.theme.MyPreview

// Figma "sign in" frame. Spacers are the gaps between the frame's y positions.
@Composable
fun LoginContent(modifier: Modifier = Modifier,
                 state: LoginContract.State,
                 snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
                 onEmailChange: (String) -> Unit = {},
                 onPasswordChange: (String) -> Unit = {},
                 onPasswordVisibilityToggle: () -> Unit = {},
                 onLoginClick: () -> Unit = {},
                 onCreateAccountClick: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val submit = {
        focusManager.clearFocus()
        onLoginClick()
    }
    AuthScreenContainer(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
    ) {
        Spacer(modifier = Modifier.height(47.dp))
        AuthLogo(modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(83.5.dp))
        Text(
            text = stringResource(R.string.login_title),
            style = AuthTitleTextStyle,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.login_subtitle),
            style = AuthSubtitleTextStyle,
        )
        Spacer(modifier = Modifier.height(40.dp))
        // labelled "User Name" as in the design, but the API signs in with an email
        AuthLabeledField(
            label = stringResource(R.string.login_user_name_label),
            errorMessage = state.emailError?.let { error -> fieldErrorMessage(error) },
        ) {
            AuthTextField(
                value = state.email,
                onValueChange = onEmailChange,
                placeholder = stringResource(R.string.login_user_name_placeholder),
                enabled = !state.isLoading,
                isError = state.emailError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    autoCorrectEnabled = false,
                ),
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        AuthLabeledField(
            label = stringResource(R.string.password_label),
            errorMessage = state.passwordError?.let { error -> fieldErrorMessage(error) },
        ) {
            AuthPasswordField(
                value = state.password,
                onValueChange = onPasswordChange,
                placeholder = stringResource(R.string.password_placeholder),
                isPasswordVisible = state.isPasswordVisible,
                onVisibilityToggle = onPasswordVisibilityToggle,
                onDone = submit,
                enabled = !state.isLoading,
                isError = state.passwordError != null,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Shown as in the design but not clickable: the reset-password flow is a follow-up
        Text(
            modifier = Modifier.align(Alignment.End),
            text = stringResource(R.string.forgot_password),
            style = AuthLinkTextStyle,
        )
        Spacer(modifier = Modifier.height(56.dp))
        AuthPrimaryButton(
            text = stringResource(R.string.login_button),
            isLoading = state.isLoading,
            onClick = submit,
        )
        Spacer(modifier = Modifier.height(32.dp))
        CreateAccountPrompt(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = !state.isLoading,
            onClick = onCreateAccountClick,
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

// "Don't have an account? Create Account": the whole line opens Register
@Composable
fun CreateAccountPrompt(modifier: Modifier = Modifier,
                        enabled: Boolean = true,
                        onClick: () -> Unit = {}) {
    Text(
        modifier = modifier.clickable(
            enabled = enabled,
            role = Role.Button,
            onClick = onClick,
        ),
        text = stringResource(R.string.login_create_account_prompt),
        style = AuthPromptTextStyle,
        textAlign = TextAlign.Center,
    )
}

@Composable
@Preview(showBackground = true, widthDp = 430, heightDp = 888)
private fun LoginContentPreview(){
    MyPreview {
        LoginContent(state = LoginContract.State())
    }
}

@Composable
@Preview(showBackground = true, widthDp = 430, heightDp = 888)
private fun LoginContentWithErrorsPreview(){
    MyPreview {
        LoginContent(
            state = LoginContract.State(
                email = "ahmedmutti@",
                emailError = FieldError.INVALID_EMAIL,
                passwordError = FieldError.REQUIRED,
            )
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 430, heightDp = 888)
private fun LoginContentLoadingPreview(){
    MyPreview {
        LoginContent(
            state = LoginContract.State(
                email = "ahmedmutti@gmail.com",
                password = "Ahmed@123",
                isPasswordVisible = true,
                isLoading = true,
            )
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF004182)
private fun CreateAccountPromptPreview(){
    MyPreview {
        CreateAccountPrompt()
    }
}
