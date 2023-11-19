package com.rrc.adev3007.pixel_perfect.the_y_app.data.viewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrc.adev3007.pixel_perfect.the_y_app.data.Synchronizer
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.Post
import kotlinx.coroutines.launch

class PostViewModel() : ViewModel() {

    val homePosts: MutableState<List<Post>> = mutableStateOf(emptyList())
    val searchedPosts: MutableState<List<Post>> = mutableStateOf(emptyList())
    val dislikedPosts: MutableState<List<Post>> = mutableStateOf(emptyList())
    val searchQuery: MutableState<String> = mutableStateOf("")

    suspend fun getHomePosts(username: String, apiKey: String) {
        val response = Synchronizer.api.getPosts(username, apiKey)
        if (response.isSuccessful) {
            homePosts.value = response.body() ?: emptyList()
        }
    }

    fun search(username: String, apiKey: String) {
        viewModelScope.launch {
            /* TODO - Replace with search API call */
            Log.i("Search", searchQuery.value)
            val response = Synchronizer.api.getPosts(username, apiKey)
            if (response.isSuccessful) {
                searchedPosts.value = response.body() ?: emptyList()
            }
        }
    }
}
