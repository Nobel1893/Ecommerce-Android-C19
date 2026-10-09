package com.route.api.dataSource

import com.route.api.ApiModule
import com.route.api.ApiServices
import com.route.data.dataSource.CategoriesOnlineDataSource
import com.route.domain.data.Category
import javax.inject.Inject

class CategoriesOnlineDataSourceImpl @Inject constructor(val apiServices: ApiServices): CategoriesOnlineDataSource {

    //Retro-fit -> Anottaions
    // OkHttp
    override suspend fun getCategories(): List<Category> {
        // call api
        return  apiServices.getCategories().data?.map {
            categoryDto ->categoryDto.toCategory()

        }?.filterNotNull() ?: listOf()

    }
}