package com.route.firstapp.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.route.domain.data.FieldError
import com.route.domain.validation.FieldValidator
import com.route.firstapp.R
import com.route.firstapp.ui.theme.AuthButtonTextStyle
import com.route.firstapp.ui.theme.AuthFieldErrorTextStyle
import com.route.firstapp.ui.theme.AuthFieldLabelTextStyle
import com.route.firstapp.ui.theme.AuthInputTextStyle
import com.route.firstapp.ui.theme.AuthPlaceholderColor
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.MyPreview

// Building blocks shared by the Login and Register screens (Figma "sign in" / "sign up" frames).
// Both screens use fixed brand colors, so they look the same in light and dark mode.

private val AuthFieldShape = RoundedCornerShape(15.dp)
private val AuthFieldHeight = 64.dp

// BrandColor over the whole window, behind the system bars too
@Composable
fun AuthBackground(modifier: Modifier = Modifier,
                   content: @Composable BoxScope.() -> Unit = {}) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColor),
        content = content,
    )
}

// Scrollable form kept clear of the system bars and the keyboard, with a snackbar for server errors
@Composable
fun AuthScreenContainer(modifier: Modifier = Modifier,
                        snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
                        content: @Composable ColumnScope.() -> Unit) {
    AuthBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            content = content,
        )
        SnackbarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .safeDrawingPadding(),
            hostState = snackbarHostState,
        )
    }
}

@Composable
fun AuthLogo(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_route_logo_white),
        contentDescription = stringResource(R.string.route_logo_description),
    )
}

// Label, 24dp gap, the field, then the field's error (if any) under it
@Composable
fun AuthLabeledField(modifier: Modifier = Modifier,
                     label: String,
                     errorMessage: String? = null,
                     field: @Composable () -> Unit) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = AuthFieldLabelTextStyle,
        )
        Spacer(modifier = Modifier.height(24.dp))
        field()
        if (errorMessage != null) {
            Text(
                modifier = Modifier.padding(start = 4.dp, top = 8.dp),
                text = errorMessage,
                style = AuthFieldErrorTextStyle,
            )
        }
    }
}

@Composable
fun AuthTextField(modifier: Modifier = Modifier,
                  value: String,
                  onValueChange: (String) -> Unit,
                  placeholder: String,
                  enabled: Boolean = true,
                  isError: Boolean = false,
                  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
                  keyboardActions: KeyboardActions = KeyboardActions.Default,
                  visualTransformation: VisualTransformation = VisualTransformation.None,
                  trailingIcon: (@Composable () -> Unit)? = null) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .height(AuthFieldHeight),
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        textStyle = AuthInputTextStyle,
        placeholder = {
            Text(text = placeholder, style = AuthInputTextStyle)
        },
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        shape = AuthFieldShape,
        colors = authTextFieldColors(),
    )
}

// White field without the Material underline in every state; errors are shown as text under the field
@Composable
private fun authTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        disabledTextColor = AuthPlaceholderColor,
        errorTextColor = Color.Black,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.White,
        errorContainerColor = Color.White,
        cursorColor = BrandColor,
        errorCursorColor = BrandColor,
        selectionColors = TextSelectionColors(
            handleColor = BrandColor,
            backgroundColor = BrandColor.copy(alpha = 0.3f),
        ),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        focusedPlaceholderColor = AuthPlaceholderColor,
        unfocusedPlaceholderColor = AuthPlaceholderColor,
        disabledPlaceholderColor = AuthPlaceholderColor,
        errorPlaceholderColor = AuthPlaceholderColor,
    )
}

// The last field of both forms, so its keyboard action is Done, which submits
@Composable
fun AuthPasswordField(modifier: Modifier = Modifier,
                      value: String,
                      onValueChange: (String) -> Unit,
                      placeholder: String,
                      isPasswordVisible: Boolean,
                      onVisibilityToggle: () -> Unit,
                      onDone: () -> Unit,
                      enabled: Boolean = true,
                      isError: Boolean = false) {
    AuthTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        enabled = enabled,
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            autoCorrectEnabled = false,
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
        trailingIcon = {
            PasswordVisibilityToggle(
                isPasswordVisible = isPasswordVisible,
                enabled = enabled,
                onClick = onVisibilityToggle,
            )
        },
    )
}

// eye-slash while the password is hidden, eye while it is shown
@Composable
fun PasswordVisibilityToggle(modifier: Modifier = Modifier,
                             isPasswordVisible: Boolean,
                             enabled: Boolean = true,
                             onClick: () -> Unit = {}) {
    // 48dp touch target; the extra end padding puts the 24dp icon 16dp from the field's edge
    IconButton(
        modifier = modifier.padding(end = 4.dp),
        onClick = onClick,
        enabled = enabled,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(
                if (isPasswordVisible) R.drawable.ic_eye else R.drawable.ic_eye_slash
            ),
            contentDescription = stringResource(
                if (isPasswordVisible) R.string.hide_password else R.string.show_password
            ),
            // keep the icon's own grey from the design
            tint = Color.Unspecified,
        )
    }
}

// White Login / Sign up button; a spinner replaces the label while the request runs
@Composable
fun AuthPrimaryButton(modifier: Modifier = Modifier,
                      text: String,
                      isLoading: Boolean = false,
                      onClick: () -> Unit = {}) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(AuthFieldHeight),
        onClick = onClick,
        enabled = !isLoading,
        shape = AuthFieldShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = BrandColor,
            // disabled only while loading, so it keeps its normal look
            disabledContainerColor = Color.White,
            disabledContentColor = BrandColor,
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = BrandColor,
                strokeWidth = 2.5.dp,
            )
        } else {
            Text(text = text, style = AuthButtonTextStyle)
        }
    }
}

@Composable
@ReadOnlyComposable
fun fieldErrorMessage(error: FieldError): String {
    return when (error) {
        FieldError.REQUIRED -> stringResource(R.string.error_field_required)
        FieldError.INVALID_EMAIL -> stringResource(R.string.error_invalid_email)
        FieldError.INVALID_MOBILE -> stringResource(R.string.error_invalid_mobile)
        FieldError.PASSWORD_TOO_SHORT -> pluralStringResource(
            R.plurals.error_password_too_short,
            FieldValidator.MIN_PASSWORD_LENGTH,
            FieldValidator.MIN_PASSWORD_LENGTH,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AuthBackgroundPreview(){
    MyPreview {
        AuthBackground()
    }
}

@Composable
@Preview(showBackground = true, widthDp = 430, heightDp = 300)
private fun AuthScreenContainerPreview(){
    MyPreview {
        AuthScreenContainer {
            Spacer(modifier = Modifier.height(41.dp))
            AuthLogo(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF004182)
private fun AuthLogoPreview(){
    MyPreview {
        AuthLogo()
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF004182, widthDp = 398)
private fun AuthLabeledFieldPreview(){
    MyPreview {
        AuthLabeledField(label = "Full Name") {
            AuthTextField(
                value = "Ahmed Abd Al-Muti",
                onValueChange = {},
                placeholder = "enter your full name",
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF004182, widthDp = 398)
private fun AuthLabeledFieldWithErrorPreview(){
    MyPreview {
        AuthLabeledField(label = "Mobile Number", errorMessage = "Enter a valid Egyptian mobile number, like 01012345678") {
            AuthTextField(
                value = "0101",
                onValueChange = {},
                placeholder = "enter your mobile no.",
                isError = true,
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF004182, widthDp = 398)
private fun AuthTextFieldPreview(){
    MyPreview {
        AuthTextField(
            value = "",
            onValueChange = {},
            placeholder = "enter your name",
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF004182, widthDp = 398)
private fun AuthPasswordFieldPreview(){
    MyPreview {
        AuthPasswordField(
            value = "Ahmed@123",
            onValueChange = {},
            placeholder = "enter your password",
            isPasswordVisible = false,
            onVisibilityToggle = {},
            onDone = {},
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF004182, widthDp = 398)
private fun AuthPasswordFieldVisiblePreview(){
    MyPreview {
        AuthPasswordField(
            value = "Ahmed@123",
            onValueChange = {},
            placeholder = "enter your password",
            isPasswordVisible = true,
            onVisibilityToggle = {},
            onDone = {},
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PasswordVisibilityTogglePreview(){
    MyPreview {
        PasswordVisibilityToggle(isPasswordVisible = false)
    }
}

@Composable
@Preview(showBackground = true)
private fun PasswordVisibilityToggleVisiblePreview(){
    MyPreview {
        PasswordVisibilityToggle(isPasswordVisible = true)
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF004182, widthDp = 398)
private fun AuthPrimaryButtonPreview(){
    MyPreview {
        AuthPrimaryButton(text = "Login")
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF004182, widthDp = 398)
private fun AuthPrimaryButtonLoadingPreview(){
    MyPreview {
        AuthPrimaryButton(text = "Login", isLoading = true)
    }
}
