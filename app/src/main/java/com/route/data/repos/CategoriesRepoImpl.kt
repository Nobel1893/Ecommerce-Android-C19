package com.route.data.repos

import com.route.data.dataSource.CategoriesOnlineDataSource
import com.route.domain.data.Category
import com.route.domain.repo.CategoriesRepo
import javax.inject.Inject

class CategoriesRepoImpl @Inject constructor(
    val categoriesOnlineDataSource: CategoriesOnlineDataSource
) : CategoriesRepo{

    override suspend fun getCategories(): List<Category> {
        return categoriesOnlineDataSource.getCategories()
    }
}