package com.route.firstapp.ui.auth.register

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
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
import com.route.firstapp.ui.theme.MyPreview

// Figma "sign up" frame. Spacers are the gaps between the frame's y positions.
@Composable
fun RegisterContent(modifier: Modifier = Modifier,
                    state: RegisterContract.State,
                    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
                    onNameChange: (String) -> Unit = {},
                    onPhoneChange: (String) -> Unit = {},
                    onEmailChange: (String) -> Unit = {},
                    onPasswordChange: (String) -> Unit = {},
                    onPasswordVisibilityToggle: () -> Unit = {},
                    onSignUpClick: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val submit = {
        focusManager.clearFocus()
        onSignUpClick()
    }
    AuthScreenContainer(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
    ) {
        Spacer(modifier = Modifier.height(41.dp))
        AuthLogo(modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(43.5.dp))
        AuthLabeledField(
            label = stringResource(R.string.register_full_name_label),
            errorMessage = state.nameError?.let { error -> fieldErrorMessage(error) },
        ) {
            AuthTextField(
                value = state.name,
                onValueChange = onNameChange,
                placeholder = stringResource(R.string.register_full_name_placeholder),
                enabled = !state.isLoading,
                isError = state.nameError != null,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        AuthLabeledField(
            label = stringResource(R.string.register_mobile_label),
            errorMessage = state.phoneError?.let { error -> fieldErrorMessage(error) },
        ) {
            AuthTextField(
                value = state.phone,
                onValueChange = onPhoneChange,
                placeholder = stringResource(R.string.register_mobile_placeholder),
                enabled = !state.isLoading,
                isError = state.phoneError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next,
                ),
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        AuthLabeledField(
            label = stringResource(R.string.register_email_label),
            errorMessage = state.emailError?.let { error -> fieldErrorMessage(error) },
        ) {
            AuthTextField(
                value = state.email,
                onValueChange = onEmailChange,
                placeholder = stringResource(R.string.register_email_placeholder),
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
        // the design has no confirm-password field; the password is sent as rePassword too
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
        Spacer(modifier = Modifier.height(56.dp))
        AuthPrimaryButton(
            text = stringResource(R.string.register_button),
            isLoading = state.isLoading,
            onClick = submit,
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
@Preview(showBackground = true, widthDp = 430, heightDp = 888)
private fun RegisterContentPreview(){
    MyPreview {
        RegisterContent(state = RegisterContract.State())
    }
}

@Composable
@Preview(showBackground = true, widthDp = 430, heightDp = 1000)
private fun RegisterContentWithErrorsPreview(){
    MyPreview {
        RegisterContent(
            state = RegisterContract.State(
                phone = "0101234",
                email = "ahmed@",
                password = "123",
                nameError = FieldError.REQUIRED,
                phoneError = FieldError.INVALID_MOBILE,
                emailError = FieldError.INVALID_EMAIL,
                passwordError = FieldError.PASSWORD_TOO_SHORT,
            )
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 430, heightDp = 888)
private fun RegisterContentLoadingPreview(){
    MyPreview {
        RegisterContent(
            state = RegisterContract.State(
                name = "Ahmed Abd Al-Muti",
                phone = "01010700701",
                email = "ahmedmuttii4012@gmail.com",
                password = "Ahmed@123",
                isLoading = true,
            )
        )
    }
}
