package com.rrc.adev3007.pixel_perfect.the_y_app.data.models

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("profile_picture")
    val profileImg: String?,
    val content: String,
    val media: String?,
    @SerializedName("created_at")
    val createdAt: String
)

data class CreatePostRequest(
    @SerializedName("api_key")
    val apiKey: String,
    val username: String,
    val content: String,
    @SerializedName("media_id")
    val mediaId: Int?
)
