package com.route.domain.usecase

import com.route.domain.data.Resource
import com.route.domain.data.SubCategory
import com.route.domain.repo.CategoriesRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSubCategoriesUseCase @Inject constructor(
    val categoriesRepo: CategoriesRepo,
) {
    fun getSubCategories(
        categoryId: String
    ): Flow<Resource<List<SubCategory>?>> {
        return categoriesRepo.getSubCategories(
            categoryId = categoryId,
        )
    }
}
