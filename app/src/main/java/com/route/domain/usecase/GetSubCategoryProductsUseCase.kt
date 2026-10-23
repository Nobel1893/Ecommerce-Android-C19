package com.route.domain.usecase

import com.route.domain.data.Product
import com.route.domain.data.Resource
import com.route.domain.repo.ProductsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSubCategoryProductsUseCase @Inject constructor(
    private val productsRepo: ProductsRepo,
) {
    fun getSubCategoryProducts(
        categoryId: String,
        subCategoryId: String,
    ): Flow<Resource<List<Product>?>> {
        return productsRepo.getSubCategoryProducts(
            categoryId = categoryId,
            subCategoryId = subCategoryId,
        )
    }
}
