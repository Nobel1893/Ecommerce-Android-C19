package com.route.data.dataSource

import com.route.domain.data.AuthSession
import com.route.domain.data.Resource
import kotlinx.coroutines.flow.Flow

// The logged-in session kept on the device
interface AuthLocalDataSource {
    // emits Success(session) once it is stored
    fun saveSession(session: AuthSession): Flow<Resource<AuthSession?>>
    // Success(null) when no session is stored
    fun getSession(): Flow<Resource<AuthSession?>>
}
