package com.route.data.repos

import com.route.data.dataSource.CategoriesOnlineDataSource
import com.route.domain.data.Category
import com.route.domain.data.Resource
import com.route.domain.data.SubCategory
import com.route.domain.repo.CategoriesRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoriesRepoImpl @Inject constructor(
    val categoriesOnlineDataSource: CategoriesOnlineDataSource
) : CategoriesRepo{

    override fun getCategories(): Flow<Resource<List<Category?>?>> {
        return categoriesOnlineDataSource.getCategories()
    }

    override fun getSubCategories(categoryId: String): Flow<Resource<List<SubCategory>?>> {
        return categoriesOnlineDataSource.getSubCategories(
            categoryId = categoryId,
        )
    }
}