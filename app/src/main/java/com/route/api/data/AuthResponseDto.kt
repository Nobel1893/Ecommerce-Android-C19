package com.route.api.data

import com.google.gson.annotations.SerializedName
import com.route.domain.data.AuthSession

// Success body of v1/auth/signin and v1/auth/signup. It isn't wrapped in BaseResponse.
data class AuthResponseDto(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: AuthUserDto? = null,

	@field:SerializedName("token")
	val token: String? = null,
){
	// null when the server sent no token: there is no session to keep
	fun toAuthSession(): AuthSession? {
		if (token.isNullOrBlank())
			return null
		return AuthSession(
			token = token,
			user = (user ?: AuthUserDto()).toUser(),
		)
	}
}
