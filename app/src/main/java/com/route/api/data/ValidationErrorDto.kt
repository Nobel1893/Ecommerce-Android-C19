package com.route.api.data

import com.google.gson.annotations.SerializedName

// "errors" object of a 400 validation response, e.g. { "value": "x", "msg": "Invalid ID ", "param": "id", "location": "params" }
data class ValidationErrorDto(

	// whatever the client sent for that field, so it can be any JSON type
	@field:SerializedName("value")
	val value: Any? = null,

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("param")
	val param: String? = null,

	@field:SerializedName("location")
	val location: String? = null,
)
