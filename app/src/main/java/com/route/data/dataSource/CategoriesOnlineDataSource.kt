package com.route.data.dataSource

import com.route.domain.data.Category
import com.route.domain.data.Resource
import kotlinx.coroutines.flow.Flow

interface CategoriesOnlineDataSource {
    fun getCategories(): Flow<Resource<List<Category?>?>>
}