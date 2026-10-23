package com.route.api

import com.route.api.data.AuthResponseDto
import com.route.api.data.BaseResponse
import com.route.api.data.CategoryDto
import com.route.api.data.ProductDto
import com.route.api.data.ProductsSortTypeDto
import com.route.api.data.SignInRequestDto
import com.route.api.data.SignUpRequestDto
import com.route.api.data.SubCategoryDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {

    @GET("v1/categories")
    suspend fun getCategories(): BaseResponse<List<CategoryDto>>

    // v1/categories/{id}/subcategories ignores the id and returns every subcategory,
    // so filter with the category query param instead
    @GET("v1/subcategories")
    suspend fun getSubCategories(
        @Query("category") categoryId: String,
    ): BaseResponse<List<SubCategoryDto>>

    @GET("v1/products")
    suspend fun getProducts(
        @Query("sort") sort: ProductsSortTypeDto,
    ): BaseResponse<List<ProductDto>>

    // auth answers aren't wrapped in BaseResponse
    @POST("v1/auth/signin")
    suspend fun signIn(
        @Body body: SignInRequestDto,
    ): AuthResponseDto

    @POST("v1/auth/signup")
    suspend fun signUp(
        @Body body: SignUpRequestDto,
    ): AuthResponseDto

}