package com.route.api.data

import com.route.domain.data.ProductsSort

enum class ProductsSortTypeDto(val value: String) {
    MOST_SELLING("-sold"),
    LATEST_ADDED("-created_at"),
    LOWEST_PRICE("price");

    // Overriding toString() ensures Retrofit @Query and @Path use the correct value
    override fun toString(): String = value
}

// Extention function
fun ProductsSort.toProductsSortDto(): ProductsSortTypeDto{
    return when (this) {
        ProductsSort.MOST_SELLING -> ProductsSortTypeDto.MOST_SELLING
        ProductsSort.LOWEST_PRICE -> ProductsSortTypeDto.LOWEST_PRICE
        ProductsSort.LATEST_ADDED -> ProductsSortTypeDto.LATEST_ADDED
    }
}