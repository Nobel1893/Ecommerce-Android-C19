package com.route.domain.validation

import com.route.domain.data.FieldError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FieldValidatorTest {

    private val validator = FieldValidator()

    @Test
    fun validateRequired_emptyOrBlank_isRequired() {
        assertEquals(FieldError.REQUIRED, validator.validateRequired(""))
        assertEquals(FieldError.REQUIRED, validator.validateRequired("   "))
    }

    @Test
    fun validateRequired_text_isValid() {
        assertNull(validator.validateRequired("Ahmed"))
    }

    @Test
    fun validateEmail_emptyOrBlank_isRequired() {
        assertEquals(FieldError.REQUIRED, validator.validateEmail(""))
        assertEquals(FieldError.REQUIRED, validator.validateEmail("  "))
    }

    @Test
    fun validateEmail_wellFormed_isValid() {
        listOf(
            "ahmedmutti@gmail.com",
            "first.last+shop@mail.example.co",
            "user_1@route-academy.com",
            "  ahmed@gmail.com  ", // surrounding spaces are trimmed before sending
        ).forEach { email ->
            assertNull(email, validator.validateEmail(email))
        }
    }

    @Test
    fun validateEmail_malformed_isInvalidEmail() {
        listOf(
            "ahmed",
            "ahmed@",
            "@gmail.com",
            "ahmed@gmail",
            "ahmed@gmail.c",
            "ahmed@gmail..com",
            "ahmed mutti@gmail.com",
            "ahmed@@gmail.com",
        ).forEach { email ->
            assertEquals(email, FieldError.INVALID_EMAIL, validator.validateEmail(email))
        }
    }

    @Test
    fun validateEgyptianMobile_emptyOrBlank_isRequired() {
        assertEquals(FieldError.REQUIRED, validator.validateEgyptianMobile(""))
        assertEquals(FieldError.REQUIRED, validator.validateEgyptianMobile(" "))
    }

    @Test
    fun validateEgyptianMobile_everyOperatorPrefix_isValid() {
        listOf("01012345678", "01112345678", "01212345678", "01512345678", " 01010700701 ")
            .forEach { phone ->
                assertNull(phone, validator.validateEgyptianMobile(phone))
            }
    }

    @Test
    fun validateEgyptianMobile_wrongPrefixLengthOrCharacters_isInvalidMobile() {
        listOf(
            "01312345678", // 013 is not a mobile prefix
            "01412345678",
            "0101234567", // 10 digits
            "010123456789", // 12 digits
            "1012345678",
            "+201012345678", // country code isn't accepted
            "0101234567a",
            "010 1234 5678",
        ).forEach { phone ->
            assertEquals(phone, FieldError.INVALID_MOBILE, validator.validateEgyptianMobile(phone))
        }
    }

    @Test
    fun validateNewPassword_emptyOrBlank_isRequired() {
        assertEquals(FieldError.REQUIRED, validator.validateNewPassword(""))
        assertEquals(FieldError.REQUIRED, validator.validateNewPassword("      "))
    }

    @Test
    fun validateNewPassword_shorterThanMinimum_isTooShort() {
        assertEquals(FieldError.PASSWORD_TOO_SHORT, validator.validateNewPassword("12345"))
    }

    @Test
    fun validateNewPassword_minimumLengthOrLonger_isValid() {
        assertEquals(6, FieldValidator.MIN_PASSWORD_LENGTH)
        assertNull(validator.validateNewPassword("123456"))
        assertNull(validator.validateNewPassword("Ahmed@123"))
    }
}
