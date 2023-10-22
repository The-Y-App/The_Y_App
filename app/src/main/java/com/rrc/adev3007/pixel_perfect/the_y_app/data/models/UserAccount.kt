package com.rrc.adev3007.pixel_perfect.the_y_app.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserAccount(
    @SerializedName("api_key")
    val apiKey: String,
    @SerializedName("dark_mode")
    val darkMode: Boolean,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("profanity_filter")
    val profanityFilter: Boolean,
    @SerializedName("profile_picture")
    val profilePicture: String?,
    @SerializedName("ui_scale")
    val uiScale: String
)

data class UserAuth(
    val email:String,
    val password: String
) : Serializable

data class UserCreate(
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val password: String
)
