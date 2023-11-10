package com.rrc.adev3007.pixel_perfect.the_y_app.data.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.rrc.adev3007.pixel_perfect.the_y_app.data.Synchronizer
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.Post

class PostViewModel() : ViewModel() {

    val homePosts: MutableState<List<Post>> = mutableStateOf(emptyList())
    val searchedPosts: MutableState<List<Post>> = mutableStateOf(emptyList())
    val dislikedPosts: MutableState<List<Post>> = mutableStateOf(emptyList())

    suspend fun getHomePosts(username: String, apiKey: String) {
        val response = Synchronizer.api.getPosts(username, apiKey)
        if (response.isSuccessful) {
            homePosts.value = response.body() ?: emptyList()
        }
    }
}
