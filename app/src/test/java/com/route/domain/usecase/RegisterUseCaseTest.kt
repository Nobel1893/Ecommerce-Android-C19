package com.route.domain.usecase

import com.route.domain.data.RegistrationDetails
import org.junit.Assert.assertEquals
import org.junit.Test

class RegisterUseCaseTest {

    @Test
    fun register_trimsNamePhoneAndEmailButNotThePassword() {
        val repo = FakeAuthRepo()

        RegisterUseCase(authRepo = repo).register(
            details = RegistrationDetails(
                name = " Ahmed Abd Al-Muti ",
                phone = " 01010700701 ",
                email = " ahmedmuttii4012@gmail.com ",
                password = " Ahmed@123 ",
            )
        )

        assertEquals(
            RegistrationDetails(
                name = "Ahmed Abd Al-Muti",
                phone = "01010700701",
                email = "ahmedmuttii4012@gmail.com",
                password = " Ahmed@123 ",
            ),
            repo.registeredDetails,
        )
    }
}
