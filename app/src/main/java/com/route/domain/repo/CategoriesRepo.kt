package com.route.domain.repo

import com.route.domain.data.Category
import com.route.domain.data.Resource
import kotlinx.coroutines.flow.Flow

interface CategoriesRepo {
    // kotlin-coroutines [Suspend] === Future Function
    fun getCategories(): Flow<Resource<List<Category?>?>>
}