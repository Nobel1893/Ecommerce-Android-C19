package com.route.api.data

import com.google.gson.annotations.SerializedName
import com.route.domain.data.ServerException

// Body of a failed request. Two shapes come back:
// { "statusMsg": "fail", "message": "Incorrect email or password" }
// { "message": "fail", "errors": { "msg": "...", "param": "...", ... } }   (validation errors)
data class ErrorResponseDto(

	@field:SerializedName("statusMsg")
	val statusMsg: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: ValidationErrorDto? = null,
){
	fun toServerException(cause: Throwable? = null): ServerException {
		return ServerException(
			serverMessage = readableMessage(),
			cause = cause,
		)
	}

	// validation errors carry their text in errors.msg and only "fail" in message
	private fun readableMessage(): String? {
		val validationMessage = errors?.msg?.trim()
		if (!validationMessage.isNullOrEmpty())
			return validationMessage
		val serverMessage = message?.trim()
		if (serverMessage.isNullOrEmpty() || serverMessage.equals(FAIL_STATUS, ignoreCase = true))
			return null
		return serverMessage
	}

	private companion object {
		const val FAIL_STATUS = "fail"
	}
}
