package com.route.domain.data

// Why a form field was rejected before anything is sent to the server.
enum class FieldError {
    REQUIRED,
    INVALID_EMAIL,
    INVALID_MOBILE,
    PASSWORD_TOO_SHORT,
}
