package com.route.api.data

import com.google.gson.annotations.SerializedName
import com.route.domain.data.RegistrationDetails

// Body of POST v1/auth/signup
data class SignUpRequestDto(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("rePassword")
	val rePassword: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,
)

// The sign up design has no confirm-password field, so the password is sent as rePassword too
fun RegistrationDetails.toSignUpRequestDto(): SignUpRequestDto {
	return SignUpRequestDto(
		name = name,
		email = email,
		password = password,
		rePassword = password,
		phone = phone,
	)
}
