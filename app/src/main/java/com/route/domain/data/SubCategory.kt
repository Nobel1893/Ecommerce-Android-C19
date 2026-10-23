package com.route.domain.data

data class SubCategory(
    val id: String,
    val name: String,
    val categoryId: String,
    // the API doesn't send subcategory images yet, so this is usually null
    val imageUrl: String? = null,
)
