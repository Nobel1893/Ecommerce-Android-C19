package com.route.firstapp.ui

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoutes{

    @Serializable
    object Home : AppRoutes()

    @Serializable
    object Login : AppRoutes()

    @Serializable
    object Register : AppRoutes()
}
