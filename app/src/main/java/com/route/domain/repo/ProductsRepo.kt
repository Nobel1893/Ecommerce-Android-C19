package com.route.domain.repo

import com.route.domain.data.Product
import com.route.domain.data.ProductsSort
import com.route.domain.data.Resource
import kotlinx.coroutines.flow.Flow

interface ProductsRepo{
    fun getProducts(sort: ProductsSort): Flow<Resource<List<Product>?>>
    fun getSubCategoryProducts(categoryId: String, subCategoryId: String): Flow<Resource<List<Product>?>>
    fun getProductDetails(productId: String): Flow<Resource<Product?>>
}
