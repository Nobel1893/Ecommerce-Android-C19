package com.route.domain.usecase

import com.route.domain.data.AuthSession
import com.route.domain.data.RegistrationDetails
import com.route.domain.data.Resource
import com.route.domain.repo.AuthRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

// Records what the use cases pass on and answers with fixed results
class FakeAuthRepo(
    private val savedSession: Resource<AuthSession?> = Resource.Success(data = null),
) : AuthRepo {

    var loginEmail: String? = null
    var loginPassword: String? = null
    var registeredDetails: RegistrationDetails? = null

    override fun login(email: String, password: String): Flow<Resource<AuthSession?>> {
        loginEmail = email
        loginPassword = password
        return flowOf(Resource.Loading())
    }

    override fun register(details: RegistrationDetails): Flow<Resource<AuthSession?>> {
        registeredDetails = details
        return flowOf(Resource.Loading())
    }

    override fun getSavedSession(): Flow<Resource<AuthSession?>> {
        return flowOf(Resource.Loading(), savedSession)
    }
}
