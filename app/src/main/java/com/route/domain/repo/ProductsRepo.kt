package com.route.domain.repo

import com.route.domain.data.Product
import com.route.domain.data.Resource
import kotlinx.coroutines.flow.Flow

interface ProductsRepo{
    fun getProducts(): Flow<Resource<List<Product>?>>
}