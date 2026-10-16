package com.route.domain.usecase

import com.route.domain.data.Category
import com.route.domain.data.Resource
import com.route.domain.repo.CategoriesRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(val repo: CategoriesRepo) {

   suspend fun getCategories(): Flow<Resource<List<Category?>?>>{
       return repo.getCategories()
   }
}