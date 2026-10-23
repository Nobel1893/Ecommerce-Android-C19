package com.route.data.repos

import com.route.data.dataSource.AuthLocalDataSource
import com.route.data.dataSource.AuthOnlineDataSource
import com.route.domain.data.AuthSession
import com.route.domain.data.RegistrationDetails
import com.route.domain.data.Resource
import com.route.domain.data.ServerException
import com.route.domain.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class AuthRepoImplTest {

    private val session = AuthSession(
        token = "jwt-token",
        user = User(name = "Ahmed Abd Al-Muti", email = "ahmedmutti@gmail.com"),
    )

    @Test
    fun login_success_savesTheSessionThenReportsIt() = runBlocking {
        val local = FakeAuthLocalDataSource()
        val repo = AuthRepoImpl(FakeAuthOnlineDataSource(Resource.Success(session)), local)

        val results = repo.login(email = "ahmedmutti@gmail.com", password = "Ahmed@123").toList()

        assertEquals(session, local.storedSession)
        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertEquals(Resource.Success(session), results[1])
    }

    @Test
    fun register_success_savesTheSession() = runBlocking {
        val local = FakeAuthLocalDataSource()
        val repo = AuthRepoImpl(FakeAuthOnlineDataSource(Resource.Success(session)), local)

        val results = repo.register(
            details = RegistrationDetails(
                name = "Ahmed Abd Al-Muti",
                phone = "01010700701",
                email = "ahmedmutti@gmail.com",
                password = "Ahmed@123",
            )
        ).toList()

        assertEquals(session, local.storedSession)
        assertEquals(Resource.Success(session), results.last())
    }

    @Test
    fun login_serverError_savesNothingAndPassesTheErrorOn() = runBlocking {
        val local = FakeAuthLocalDataSource()
        val error = ServerException(serverMessage = "Incorrect email or password")
        val repo = AuthRepoImpl(FakeAuthOnlineDataSource(Resource.Error(error)), local)

        val result = repo.login(email = "ahmedmutti@gmail.com", password = "wrong").toList().last()

        assertNull(local.storedSession)
        assertEquals(Resource.Error<AuthSession?>(error), result)
    }

    @Test
    fun login_successWithoutSession_savesNothing() = runBlocking {
        val local = FakeAuthLocalDataSource()
        val repo = AuthRepoImpl(FakeAuthOnlineDataSource(Resource.Success(null)), local)

        val result = repo.login(email = "ahmedmutti@gmail.com", password = "Ahmed@123").toList().last()

        assertNull(local.storedSession)
        assertEquals(Resource.Success<AuthSession?>(null), result)
    }

    @Test
    fun login_saveFails_reportsTheSaveError() = runBlocking {
        val saveError = IOException("disk full")
        val local = FakeAuthLocalDataSource(saveError = saveError)
        val repo = AuthRepoImpl(FakeAuthOnlineDataSource(Resource.Success(session)), local)

        val results = repo.login(email = "ahmedmutti@gmail.com", password = "Ahmed@123").toList()

        // one Loading only: the save's own Loading isn't passed on
        assertEquals(2, results.size)
        assertEquals(Resource.Error<AuthSession?>(saveError), results[1])
    }

    @Test
    fun getSavedSession_readsTheLocalSession() = runBlocking {
        val local = FakeAuthLocalDataSource(storedSession = session)
        val repo = AuthRepoImpl(FakeAuthOnlineDataSource(Resource.Success(null)), local)

        assertEquals(Resource.Success(session), repo.getSavedSession().toList().last())
    }

    private class FakeAuthOnlineDataSource(
        private val result: Resource<AuthSession?>,
    ) : AuthOnlineDataSource {
        override fun signIn(email: String, password: String): Flow<Resource<AuthSession?>> {
            return flowOf(Resource.Loading(), result)
        }

        override fun signUp(details: RegistrationDetails): Flow<Resource<AuthSession?>> {
            return flowOf(Resource.Loading(), result)
        }
    }

    private class FakeAuthLocalDataSource(
        var storedSession: AuthSession? = null,
        private val saveError: Throwable? = null,
    ) : AuthLocalDataSource {
        override fun saveSession(session: AuthSession): Flow<Resource<AuthSession?>> {
            return flow {
                emit(Resource.Loading())
                if (saveError != null) {
                    emit(Resource.Error(saveError))
                } else {
                    storedSession = session
                    emit(Resource.Success(session))
                }
            }
        }

        override fun getSession(): Flow<Resource<AuthSession?>> {
            return flowOf(Resource.Loading(), Resource.Success(storedSession))
        }
    }
}
