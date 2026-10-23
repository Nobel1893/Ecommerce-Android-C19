package com.route.domain.usecase

import com.route.domain.data.LoginFormErrors
import com.route.domain.validation.FieldValidator
import javax.inject.Inject

class ValidateLoginFormUseCase @Inject constructor(
    private val fieldValidator: FieldValidator,
) {
    // Login only checks that the password is there: the server decides whether it is right
    fun validateLoginForm(email: String, password: String): LoginFormErrors {
        return LoginFormErrors(
            email = fieldValidator.validateEmail(email),
            password = fieldValidator.validateRequired(password),
        )
    }
}
