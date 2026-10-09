package com.route.api

import com.route.api.data.BaseResponse
import com.route.api.data.CategoryDto
import retrofit2.http.GET

interface ApiServices {

    @GET("v1/categories")
    suspend fun getCategories(): BaseResponse<List<CategoryDto>>

}