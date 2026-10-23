package com.route.local.dataSource

import android.content.SharedPreferences
import com.route.IoDispatcher
import com.route.data.dataSource.AuthLocalDataSource
import com.route.domain.data.AuthSession
import com.route.domain.data.Resource
import com.route.domain.data.User
import com.route.local.AuthPreferences
import dagger.Lazy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor(
    // Lazy: opening the file reads the disk, so it happens on the first IO-dispatched call, not at injection
    @AuthPreferences private val preferences: Lazy<SharedPreferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AuthLocalDataSource {

    override fun saveSession(session: AuthSession): Flow<Resource<AuthSession?>> {
        return safeLocalCall {
            writeSession(session)
            session
        }
    }

    override fun getSession(): Flow<Resource<AuthSession?>> {
        return safeLocalCall { readSession() }
    }

    private fun writeSession(session: AuthSession) {
        val isSaved = preferences.get().edit()
            .putString(KEY_TOKEN, session.token)
            .putString(KEY_USER_NAME, session.user.name)
            .putString(KEY_USER_EMAIL, session.user.email)
            .commit()
        if (!isSaved) throw IOException("Couldn't save the session")
    }

    private fun readSession(): AuthSession? {
        val storedPreferences = preferences.get()
        val token = storedPreferences.getString(KEY_TOKEN, null)
        if (token.isNullOrBlank()) return null
        return AuthSession(
            token = token,
            user = User(
                name = storedPreferences.getString(KEY_USER_NAME, null) ?: "",
                email = storedPreferences.getString(KEY_USER_EMAIL, null) ?: "",
            ),
        )
    }

    // Same contract as safeApiCallV2: Loading, then Success or Error, never a thrown exception
    private fun <T> safeLocalCall(block: () -> T): Flow<Resource<T?>> {
        return flow {
            emit(Resource.Loading())
            val resource: Resource<T?> = try {
                Resource.Success(data = block())
            } catch (e: Exception) {
                Resource.Error(e)
            }
            emit(resource)
        }.flowOn(ioDispatcher)
    }

    private companion object {
        const val KEY_TOKEN = "token"
        const val KEY_USER_NAME = "user_name"
        const val KEY_USER_EMAIL = "user_email"
    }
}
