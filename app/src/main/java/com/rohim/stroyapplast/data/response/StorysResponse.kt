package com.rohim.stroyapplast.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StorysResponse(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

//@Entity(tableName = "story")
@Parcelize
data class ListStoryItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("createdAt")
	val createdAt: String?,

	@field:SerializedName("name")
	val name: String?,

	@field:SerializedName("description")
	val description: String?,

	@field:SerializedName("lon")
	val lon: Double,

//	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("lat")
	val lat: Double
) : Parcelable
