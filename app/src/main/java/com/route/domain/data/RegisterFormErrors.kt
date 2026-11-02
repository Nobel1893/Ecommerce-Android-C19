package com.route.domain.data

data class RegisterFormErrors(
    val name: FieldError? = null,
    val phone: FieldError? = null,
    val email: FieldError? = null,
    val password: FieldError? = null,
) {
    val isValid: Boolean
        get() = name == null && phone == null && email == null && password == null
}
