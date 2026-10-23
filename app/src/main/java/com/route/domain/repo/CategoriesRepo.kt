package com.route.domain.repo

import com.route.domain.data.Category
import com.route.domain.data.Resource
import com.route.domain.data.SubCategory
import kotlinx.coroutines.flow.Flow

interface CategoriesRepo {
    // kotlin-coroutines [Suspend] === Future Function
    fun getCategories(): Flow<Resource<List<Category?>?>>
    fun getSubCategories(categoryId: String): Flow<Resource<List<SubCategory>?>>
}