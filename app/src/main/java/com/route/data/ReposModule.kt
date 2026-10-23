package com.route.data

import com.route.data.repos.AuthRepoImpl
import com.route.data.repos.CategoriesRepoImpl
import com.route.data.repos.ProductsRepoImpl
import com.route.domain.repo.AuthRepo
import com.route.domain.repo.CategoriesRepo
import com.route.domain.repo.ProductsRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class ReposModule {

    @Binds
    abstract fun provideCategoriesRepo(
        repo: CategoriesRepoImpl
    ): CategoriesRepo

    @Binds
    abstract fun provideProductsRepo(
        repo: ProductsRepoImpl
    ): ProductsRepo

    @Binds
    abstract fun provideAuthRepo(
        repo: AuthRepoImpl
    ): AuthRepo
}