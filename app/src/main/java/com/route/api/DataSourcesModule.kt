package com.route.api

import com.route.api.dataSource.CategoriesOnlineDataSourceImpl
import com.route.data.dataSource.CategoriesOnlineDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourcesModule {

    @Binds
    abstract fun provideCategoriesDataSource(
        dataSource : CategoriesOnlineDataSourceImpl
    ): CategoriesOnlineDataSource

}