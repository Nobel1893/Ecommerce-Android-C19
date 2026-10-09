package com.route.data.dataSource

import com.route.domain.data.Category

interface CategoriesOnlineDataSource {
    suspend fun getCategories(): List<Category>
}