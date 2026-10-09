package com.route.domain.repo

import com.route.domain.data.Category

interface CategoriesRepo {
    // kotlin-coroutines [Suspend] === Future Function
    suspend fun getCategories(): List<Category>
}