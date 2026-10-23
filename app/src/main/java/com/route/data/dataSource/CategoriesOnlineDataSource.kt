package com.route.data.dataSource

import com.route.domain.data.Category
import com.route.domain.data.Resource
import com.route.domain.data.SubCategory
import kotlinx.coroutines.flow.Flow

interface CategoriesOnlineDataSource {
    fun getCategories(): Flow<Resource<List<Category?>?>>
    fun getSubCategories(categoryId: String): Flow<Resource<List<SubCategory>?>>
}