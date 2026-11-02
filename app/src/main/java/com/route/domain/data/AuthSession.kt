package com.route.domain.data

// What sign in / sign up returns and what is kept on the device while the user is logged in.
data class AuthSession(
    val token: String,
    val user: User,
)
