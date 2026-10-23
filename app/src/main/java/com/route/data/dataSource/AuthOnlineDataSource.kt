package com.route.data.dataSource

import com.route.domain.data.AuthSession
import com.route.domain.data.RegistrationDetails
import com.route.domain.data.Resource
import kotlinx.coroutines.flow.Flow

interface AuthOnlineDataSource {
    fun signIn(email: String, password: String): Flow<Resource<AuthSession?>>
    fun signUp(details: RegistrationDetails): Flow<Resource<AuthSession?>>
}
