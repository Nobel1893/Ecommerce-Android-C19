package com.route.domain.usecase

import com.route.domain.data.Category
import com.route.domain.repo.CategoriesRepo
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(val repo: CategoriesRepo) {

   suspend fun getCategories():List<Category>{
       return repo.getCategories()
   }
}