package com.rrc.adev3007.pixel_perfect.the_y_app.data.viewModels

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
    val isFetchInProgress: MutableState<Boolean> = mutableStateOf(false)

    suspend fun getHomePosts(username: String, apiKey: String): Boolean {
        isFetchInProgress.value = true
        val response = Synchronizer.api {
            getPosts(username, apiKey)
        }
        homePosts.value = response?.body() ?: emptyList()
        isFetchInProgress.value = false
        return response != null
    }

    suspend fun search(username: String, apiKey: String): Boolean {
        isFetchInProgress.value = true
        val response = Synchronizer.api {
            getPosts(username, apiKey, searchQuery.value)
        }
        searchedPosts.value = response?.body() ?: emptyList()
        isFetchInProgress.value = false
        return response != null
    }

    suspend fun getDislikedPosts(username: String, apiKey: String): Boolean {
        isFetchInProgress.value = true
        val response = Synchronizer.api {
            getPosts(
                username = username,
                api_key = apiKey,
                isDislikesOnly = true
            )
        }
        dislikedPosts.value = response?.body() ?: emptyList()
        isFetchInProgress.value = false
        return response != null
    }

    fun downVote(postId: Int, username: String, apiKey: String) {
        viewModelScope.launch {
            Synchronizer.api {
                addPostDislike(
                    postId,
                    mapOf("username" to username, "api_key" to apiKey)
                )
            }
        }
    }

    fun deleteDownvote(postId: Int, username: String, apiKey: String) {
        viewModelScope.launch {
            Synchronizer.api {
                deleteDownvote(
                    postId,
                    mapOf("username" to username, "api_key" to apiKey)
                )
            }
        }
    }
}
