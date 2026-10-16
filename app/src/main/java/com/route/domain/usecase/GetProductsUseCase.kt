package com.route.domain.usecase

import com.route.domain.data.Product
import com.route.domain.data.ProductsSort
import com.route.domain.data.Resource
import com.route.domain.repo.ProductsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    val productsRepo: ProductsRepo
) {
    fun getSortedProducts(
        sort: ProductsSort
    ): Flow<Resource<List<Product>?>>{
        return productsRepo.getProducts(
            sort = sort,
        )
    }
}