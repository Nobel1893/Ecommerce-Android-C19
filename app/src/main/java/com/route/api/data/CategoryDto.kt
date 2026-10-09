package com.route.api.data

import com.google.gson.annotations.SerializedName
import com.route.domain.data.Category

data class CategoryDto(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
){

	fun toCategory(): Category? {
		if(id == null || name == null || image == null)
			return null

		return Category(id = this.id,
			name = this.name,
			imageUrl = this.image)
	}
}