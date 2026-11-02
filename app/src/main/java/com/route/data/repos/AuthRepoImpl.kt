package com.route.data.repos

import com.route.data.dataSource.AuthLocalDataSource
import com.route.data.dataSource.AuthOnlineDataSource
import com.route.domain.data.AuthSession
import com.route.domain.data.RegistrationDetails
import com.route.domain.data.Resource
import com.route.domain.repo.AuthRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val authOnlineDataSource: AuthOnlineDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) : AuthRepo {

    override fun login(email: String, password: String): Flow<Resource<AuthSession?>> {
        return authOnlineDataSource.signIn(email = email, password = password)
            .saveSessionOnSuccess()
    }

    override fun register(details: RegistrationDetails): Flow<Resource<AuthSession?>> {
        return authOnlineDataSource.signUp(details = details)
            .saveSessionOnSuccess()
    }

    override fun getSavedSession(): Flow<Resource<AuthSession?>> {
        return authLocalDataSource.getSession()
    }

    // The user only counts as logged in once the session is stored, so a failed save is reported
    // as the result instead of the server's success.
    private fun Flow<Resource<AuthSession?>>.saveSessionOnSuccess(): Flow<Resource<AuthSession?>> {
        return transform { resource ->
            val session = (resource as? Resource.Success)?.data
            if (session == null) {
                emit(resource)
            } else {
                emitAll(
                    authLocalDataSource.saveSession(session)
                        .filterNot { saveResource -> saveResource is Resource.Loading }
                )
            }
        }
    }
}
