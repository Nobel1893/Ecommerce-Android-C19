package com.route.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

class LoginUseCaseTest {

    @Test
    fun login_trimsTheEmailButNotThePassword() {
        val repo = FakeAuthRepo()

        LoginUseCase(authRepo = repo).login(email = "  ahmedmutti@gmail.com ", password = " Ahmed@123 ")

        assertEquals("ahmedmutti@gmail.com", repo.loginEmail)
        assertEquals(" Ahmed@123 ", repo.loginPassword)
    }
}
