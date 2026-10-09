package com.route.api.data

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(

	@field:SerializedName("metadata")
	val metadata: Metadata? = null,

	@field:SerializedName("data")
	val data: T? = null,

	@field:SerializedName("results")
	val results: Int? = null
)