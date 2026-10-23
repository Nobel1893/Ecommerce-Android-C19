package com.route.domain.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductTest {

    @Test
    fun finalPrice_isDiscountedPriceWhenThereIsADiscount() {
        assertEquals(349, Product(price = 499, priceAfterDiscount = 349).finalPrice)
    }

    @Test
    fun finalPrice_isPriceWithoutADiscount() {
        assertEquals(499, Product(price = 499).finalPrice)
    }

    @Test
    fun finalPrice_isNullWithoutAnyPrice() {
        assertNull(Product().finalPrice)
    }
}
