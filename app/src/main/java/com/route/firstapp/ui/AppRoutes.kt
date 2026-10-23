package com.route.firstapp.ui

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoutes{

    @Serializable
    object HomeTab : AppRoutes()
    @Serializable
    object CategoriesTab : AppRoutes()
    @Serializable
    object FavoritesTab : AppRoutes()
    @Serializable
    object ProfileTab : AppRoutes()


    @Serializable
    object Home : AppRoutes()

    // Route for a screen requiring data
    @Serializable
    data class ProductDetails(val productId: String): AppRoutes()

    @Serializable
    data class ProductsCatalog(val categoryId: String, val subCategoryId: String): AppRoutes()
}