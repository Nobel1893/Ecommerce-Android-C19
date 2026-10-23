package com.route.domain.data

data class LoginFormErrors(
    val email: FieldError? = null,
    val password: FieldError? = null,
) {
    val isValid: Boolean
        get() = email == null && password == null
}
