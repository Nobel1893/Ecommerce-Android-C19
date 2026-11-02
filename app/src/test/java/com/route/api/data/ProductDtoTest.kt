package com.route.api.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductDtoTest {

    @Test
    fun toProduct_keepsDiscountedPrice() {
        val product = requireNotNull(ProductDto(id = "1", price = 499, priceAfterDiscount = 349).toProduct())

        assertEquals(349, product.priceAfterDiscount)
    }

    @Test
    fun toProduct_treatsZeroDiscountedPriceAsNoDiscount() {
        val product = requireNotNull(ProductDto(id = "1", price = 8999, priceAfterDiscount = 0).toProduct())

        assertNull(product.priceAfterDiscount)
    }

    @Test
    fun toProduct_treatsMissingDiscountedPriceAsNoDiscount() {
        val product = requireNotNull(ProductDto(id = "1", price = 149).toProduct())

        assertNull(product.priceAfterDiscount)
    }

    @Test
    fun toProduct_isNullWithoutAnId() {
        val product = ProductDto(title = "Woman Shawl", price = 499).toProduct()

        assertNull(product)
    }

    @Test
    fun toProduct_mapsDetailsFields() {
        val product = requireNotNull(
            ProductDto(
                id = "6428ebc6dc1175abc65ca0b9",
                title = "Woman Shawl",
                imageCover = "cover.jpeg",
                description = "Material Polyester Blend",
                ratingsAverage = 4.1,
                ratingsQuantity = 47,
                sold = 2973,
                quantity = 220,
                images = listOf("1.jpeg", null, "2.jpeg"),
            ).toProduct()
        )

        assertEquals("6428ebc6dc1175abc65ca0b9", product.id)
        assertEquals("Woman Shawl", product.name)
        assertEquals("cover.jpeg", product.imageUrl)
        assertEquals("Material Polyester Blend", product.description)
        assertEquals(4.1, product.ratingsAverage)
        assertEquals(47, product.ratingsQuantity)
        assertEquals(2973, product.soldCount)
        assertEquals(220, product.availableQuantity)
        assertEquals(listOf("1.jpeg", "2.jpeg"), product.images)
    }
}
