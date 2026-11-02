package com.route.domain.usecase

import com.route.domain.data.FieldError
import com.route.domain.data.LoginFormErrors
import com.route.domain.validation.FieldValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateLoginFormUseCaseTest {

    private val useCase = ValidateLoginFormUseCase(fieldValidator = FieldValidator())

    @Test
    fun emptyForm_bothFieldsRequired() {
        val errors = useCase.validateLoginForm(email = "", password = "")

        assertEquals(LoginFormErrors(email = FieldError.REQUIRED, password = FieldError.REQUIRED), errors)
        assertFalse(errors.isValid)
    }

    @Test
    fun malformedEmail_isInvalidEmail() {
        val errors = useCase.validateLoginForm(email = "ahmed@", password = "Ahmed@123")

        assertEquals(LoginFormErrors(email = FieldError.INVALID_EMAIL), errors)
    }

    @Test
    fun shortPassword_isAccepted_becauseTheServerDecidesOnLogin() {
        val errors = useCase.validateLoginForm(email = "ahmedmutti@gmail.com", password = "123")

        assertTrue(errors.isValid)
    }

    @Test
    fun validForm_hasNoErrors() {
        val errors = useCase.validateLoginForm(email = "ahmedmutti@gmail.com", password = "Ahmed@123")

        assertEquals(LoginFormErrors(), errors)
        assertTrue(errors.isValid)
    }
}
