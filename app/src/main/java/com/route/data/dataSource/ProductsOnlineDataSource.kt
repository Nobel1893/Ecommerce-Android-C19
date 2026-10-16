package com.route.data.dataSource

import com.route.domain.data.Product
import com.route.domain.data.ProductsSort
import com.route.domain.data.Resource
import kotlinx.coroutines.flow.Flow

interface ProductsOnlineDataSource {
    fun getProducts(sort: ProductsSort): Flow<Resource<List<Product>?>>
}