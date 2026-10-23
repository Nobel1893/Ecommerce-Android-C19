package com.route.domain.usecase

import com.route.domain.data.AuthSession
import com.route.domain.data.Resource
import com.route.domain.data.User
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class IsLoggedInUseCaseTest {

    @Test
    fun savedSession_isLoggedIn() = runBlocking {
        val session = AuthSession(token = "token", user = User(name = "Ahmed", email = "ahmed@gmail.com"))
        val useCase = IsLoggedInUseCase(authRepo = FakeAuthRepo(savedSession = Resource.Success(session)))

        assertEquals(Resource.Success(data = true), useCase.isLoggedIn().last())
    }

    @Test
    fun noSavedSession_isLoggedOut() = runBlocking {
        val useCase = IsLoggedInUseCase(authRepo = FakeAuthRepo(savedSession = Resource.Success(null)))

        assertEquals(Resource.Success(data = false), useCase.isLoggedIn().last())
    }

    @Test
    fun unreadableSession_isPassedOnAsError() = runBlocking {
        val failure = IOException("disk")
        val useCase = IsLoggedInUseCase(authRepo = FakeAuthRepo(savedSession = Resource.Error(failure)))

        val result = useCase.isLoggedIn().last()

        assertTrue(result is Resource.Error)
        assertEquals(failure, (result as Resource.Error).throwable)
    }
}
