package com.route.data

import com.route.data.repos.CategoriesRepoImpl
import com.route.domain.repo.CategoriesRepo
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
}