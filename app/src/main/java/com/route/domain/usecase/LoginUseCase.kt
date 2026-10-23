package com.route.domain.usecase

import com.route.domain.data.AuthSession
import com.route.domain.data.Resource
import com.route.domain.repo.AuthRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepo: AuthRepo,
) {
    fun login(email: String, password: String): Flow<Resource<AuthSession?>> {
        return authRepo.login(
            email = email.trim(),
            password = password,
        )
    }
}
