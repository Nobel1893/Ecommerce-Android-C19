package com.route.api

import com.route.api.dataSource.AuthOnlineDataSourceImpl
import com.route.api.dataSource.CategoriesOnlineDataSourceImpl
import com.route.api.dataSource.ProductsOnlineDataSourceImpl
import com.route.data.dataSource.AuthOnlineDataSource
import com.route.data.dataSource.CategoriesOnlineDataSource
import com.route.data.dataSource.ProductsOnlineDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourcesModule {

    @Binds
    abstract fun provideCategoriesDataSource(
        dataSource : CategoriesOnlineDataSourceImpl
    ): CategoriesOnlineDataSource

    @Binds
    abstract fun provideProductsDataSource(
        dataSource : ProductsOnlineDataSourceImpl
    ): ProductsOnlineDataSource

    @Binds
    abstract fun provideAuthDataSource(
        dataSource : AuthOnlineDataSourceImpl
    ): AuthOnlineDataSource
}

//@Module
//object DataSourcesModule{
//
//    @Provides
//    fun provideProductsOnLineDataSource(
//        apiServices: ApiServices
//    ): ProductsOnlineDataSource{
//        return ProductsOnlineDataSourceImpl(
//            apiServices
//        )
//    }
//
//}