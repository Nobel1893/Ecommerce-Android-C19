package com.route.domain.repo

import com.route.domain.data.AuthSession
import com.route.domain.data.RegistrationDetails
import com.route.domain.data.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    // login and register save the returned session before reporting success
    fun login(email: String, password: String): Flow<Resource<AuthSession?>>
    fun register(details: RegistrationDetails): Flow<Resource<AuthSession?>>
    // Success(null) when nobody is logged in on this device
    fun getSavedSession(): Flow<Resource<AuthSession?>>
}
