package com.route.domain.usecase

import com.route.domain.data.FieldError
import com.route.domain.data.RegisterFormErrors
import com.route.domain.data.RegistrationDetails
import com.route.domain.validation.FieldValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateRegisterFormUseCaseTest {

    private val useCase = ValidateRegisterFormUseCase(fieldValidator = FieldValidator())

    private val validDetails = RegistrationDetails(
        name = "Ahmed Abd Al-Muti",
        phone = "01010700701",
        email = "ahmedmuttii4012@gmail.com",
        password = "Ahmed@123",
    )

    @Test
    fun emptyForm_everyFieldRequired() {
        val errors = useCase.validateRegisterForm(
            details = RegistrationDetails(name = "", phone = "", email = "", password = "")
        )

        assertEquals(
            RegisterFormErrors(
                name = FieldError.REQUIRED,
                phone = FieldError.REQUIRED,
                email = FieldError.REQUIRED,
                password = FieldError.REQUIRED,
            ),
            errors,
        )
        assertFalse(errors.isValid)
    }

    @Test
    fun invalidValues_eachFieldReportsItsOwnError() {
        val errors = useCase.validateRegisterForm(
            details = validDetails.copy(phone = "01312345678", email = "ahmed@gmail", password = "12345")
        )

        assertEquals(
            RegisterFormErrors(
                phone = FieldError.INVALID_MOBILE,
                email = FieldError.INVALID_EMAIL,
                password = FieldError.PASSWORD_TOO_SHORT,
            ),
            errors,
        )
    }

    @Test
    fun validForm_hasNoErrors() {
        val errors = useCase.validateRegisterForm(details = validDetails)

        assertEquals(RegisterFormErrors(), errors)
        assertTrue(errors.isValid)
    }
}
