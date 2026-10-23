package com.route.api.data

import com.google.gson.annotations.SerializedName
import com.route.domain.data.SubCategory

data class SubCategoryDto(

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	// not returned by the API today (checked 2026-10-23), read it in case it's added
	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
){

	fun toSubCategory(): SubCategory? {
		if(id == null || name == null || category == null)
			return null

		return SubCategory(id = this.id,
			name = this.name,
			categoryId = this.category,
			imageUrl = this.image)
	}
}
