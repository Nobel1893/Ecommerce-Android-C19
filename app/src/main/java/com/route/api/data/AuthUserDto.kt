package com.route.api.data

import com.google.gson.annotations.SerializedName
import com.route.domain.data.User

data class AuthUserDto(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("role")
	val role: String? = null,
){
	fun toUser(): User {
		return User(
			name = name ?: "",
			email = email ?: "",
		)
	}
}
