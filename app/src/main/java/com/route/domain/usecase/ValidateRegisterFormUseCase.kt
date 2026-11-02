package com.route.domain.usecase

import com.route.domain.data.RegisterFormErrors
import com.route.domain.data.RegistrationDetails
import com.route.domain.validation.FieldValidator
import javax.inject.Inject

class ValidateRegisterFormUseCase @Inject constructor(
    private val fieldValidator: FieldValidator,
) {
    fun validateRegisterForm(details: RegistrationDetails): RegisterFormErrors {
        return RegisterFormErrors(
            name = fieldValidator.validateRequired(details.name),
            phone = fieldValidator.validateEgyptianMobile(details.phone),
            email = fieldValidator.validateEmail(details.email),
            password = fieldValidator.validateNewPassword(details.password),
        )
    }
}
