package com.route.domain.usecase

import com.route.domain.data.Resource
import com.route.domain.repo.AuthRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val authRepo: AuthRepo,
) {
    fun isLoggedIn(): Flow<Resource<Boolean?>> {
        return authRepo.getSavedSession()
            .map { resource ->
                when (resource) {
                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> Resource.Success(data = resource.data != null)
                    is Resource.Error -> Resource.Error(resource.throwable)
                }
            }
    }
}
