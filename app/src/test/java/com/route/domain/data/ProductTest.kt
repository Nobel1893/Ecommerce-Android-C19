package com.route.domain.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductTest {

    @Test
    fun finalPrice_isDiscountedPriceWhenThereIsADiscount() {
        assertEquals(349, Product(id = "1", price = 499, priceAfterDiscount = 349).finalPrice)
    }

    @Test
    fun finalPrice_isPriceWithoutADiscount() {
        assertEquals(499, Product(id = "1", price = 499).finalPrice)
    }

    @Test
    fun finalPrice_isNullWithoutAnyPrice() {
        assertNull(Product(id = "1").finalPrice)
    }
}
