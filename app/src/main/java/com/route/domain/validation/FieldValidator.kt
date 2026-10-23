package com.route.domain.validation

import com.route.domain.data.FieldError
import javax.inject.Inject

// Client-side checks for the auth forms. Each function returns the first problem found, or null when the value is fine.
class FieldValidator @Inject constructor() {

    fun validateRequired(value: String): FieldError? {
        return if (value.isBlank()) FieldError.REQUIRED else null
    }

    fun validateEmail(email: String): FieldError? {
        return when {
            email.isBlank() -> FieldError.REQUIRED
            !EMAIL_REGEX.matches(email.trim()) -> FieldError.INVALID_EMAIL
            else -> null
        }
    }

    fun validateEgyptianMobile(phone: String): FieldError? {
        return when {
            phone.isBlank() -> FieldError.REQUIRED
            !EGYPTIAN_MOBILE_REGEX.matches(phone.trim()) -> FieldError.INVALID_MOBILE
            else -> null
        }
    }

    fun validateNewPassword(password: String): FieldError? {
        return when {
            password.isBlank() -> FieldError.REQUIRED
            password.length < MIN_PASSWORD_LENGTH -> FieldError.PASSWORD_TOO_SHORT
            else -> null
        }
    }

    companion object {
        // The API docs state no password rule for signup, so this is the app's own minimum
        const val MIN_PASSWORD_LENGTH = 6

        private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$")

        // 010, 011, 012 or 015 followed by 8 digits
        private val EGYPTIAN_MOBILE_REGEX = Regex("^01[0125][0-9]{8}$")
    }
}
