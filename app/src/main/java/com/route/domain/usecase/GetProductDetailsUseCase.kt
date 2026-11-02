package com.route.domain.usecase

import com.route.domain.data.Product
import com.route.domain.data.Resource
import com.route.domain.repo.ProductsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(
    private val productsRepo: ProductsRepo,
) {
    fun getProductDetails(
        productId: String,
    ): Flow<Resource<Product?>> {
        return productsRepo.getProductDetails(
            productId = productId,
        )
    }
}
