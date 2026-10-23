package com.route.api.dataSource

import com.route.api.ApiServices
import com.route.api.data.CategoryDto
import com.route.api.helpers.safeApiCallV2
import com.route.data.dataSource.CategoriesOnlineDataSource
import com.route.domain.data.Category
import com.route.domain.data.Resource
import com.route.domain.data.SubCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoriesOnlineDataSourceImpl @Inject constructor(val apiServices: ApiServices): CategoriesOnlineDataSource {

    override fun getCategories(): Flow<Resource<List<Category?>?>> {
        return safeApiCallV2<List<CategoryDto>,List<Category?>>(
            api = {
                apiServices.getCategories()
            },
            converter = { dtos ->
                dtos?.mapNotNull { dto -> dto.toCategory() }?: listOf()
            }
        )
    }

    override fun getSubCategories(categoryId: String): Flow<Resource<List<SubCategory>?>> {
        return safeApiCallV2(
            api = {
                apiServices.getSubCategories(categoryId = categoryId)
            },
            converter = { dtos ->
                dtos?.mapNotNull { dto -> dto.toSubCategory() }?: listOf()
            }
        )
    }
    //Retro-fit -> Anottaions
    // OkHttp
//    override fun getCategories(): Flow<Resource<List<Category>>> {
//        return safeApiCall{
//            apiServices.getCategories()
//        }.map { response ->
//            when (response) {
//                is Resource.Success -> {
//                    val convertedData = response.data?.mapNotNull { dto -> dto.toCategory() } ?: listOf()
//                    return@map Resource.Success(data = convertedData)
//                }
//                is Resource.Error<*> -> {
//                    return@map Resource.Error(throwable = response.throwable)
//                }
//                is Resource.Loading<*> -> {
//                    return@map Resource.Loading()
//                }
//            }
//        }
//    }

}