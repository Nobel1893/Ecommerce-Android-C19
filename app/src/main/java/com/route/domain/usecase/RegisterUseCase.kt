package com.route.domain.usecase

import com.route.domain.data.AuthSession
import com.route.domain.data.RegistrationDetails
import com.route.domain.data.Resource
import com.route.domain.repo.AuthRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepo: AuthRepo,
) {
    fun register(details: RegistrationDetails): Flow<Resource<AuthSession?>> {
        return authRepo.register(
            details = details.copy(
                name = details.name.trim(),
                phone = details.phone.trim(),
                email = details.email.trim(),
            )
        )
    }
}
