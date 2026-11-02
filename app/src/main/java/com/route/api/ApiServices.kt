package com.route.api

import com.route.api.data.BaseResponse
import com.route.api.data.CategoryDto
import com.route.api.data.ProductDto
import com.route.api.data.ProductsSortTypeDto
import com.route.api.data.SubCategoryDto
import retrofit2.http.GET
import retrofit2.http.Path
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

    // unknown query params make the API return no products, so keep these names exact
    @GET("v1/products")
    suspend fun getSubCategoryProducts(
        @Query("category") categoryId: String,
        @Query("subcategory") subCategoryId: String,
    ): BaseResponse<List<ProductDto>>

    @GET("v1/products/{id}")
    suspend fun getProductDetails(
        @Path("id") productId: String,
    ): BaseResponse<ProductDto>

}