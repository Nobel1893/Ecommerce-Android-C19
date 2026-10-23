package com.route.domain.data

data class Product(
    val id: String,
    val name:String? = null,
    val imageUrl:String? = null,
    val description: String? = null,
    // prices are in EGP
    val price: Int? = null,
    // null when the product has no discount
    val priceAfterDiscount: Int? = null,
    val ratingsAverage: Double? = null,
    val ratingsQuantity: Int? = null,
    val soldCount: Int? = null,
    // items in stock
    val availableQuantity: Int? = null,
    val images: List<String> = listOf(),
){
    // what one item costs after any discount
    val finalPrice: Int?
        get() = priceAfterDiscount ?: price
}
