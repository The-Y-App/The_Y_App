package com.rrc.adev3007.pixel_perfect.the_y_app.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rrc.adev3007.pixel_perfect.the_y_app.components.PostItem
import com.rrc.adev3007.pixel_perfect.the_y_app.data.viewModels.PostViewModel
import com.rrc.adev3007.pixel_perfect.the_y_app.helpers.convertToRelativeTime
import com.rrc.adev3007.pixel_perfect.the_y_app.session.SessionViewModel

@Composable
fun Home(viewModel: PostViewModel, sessionViewModel: SessionViewModel) {
    val posts by viewModel.homePosts
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        if (posts.isEmpty()) {
            viewModel.getHomePosts(sessionViewModel.username.value, sessionViewModel.apiKey.value)
        }
    }
    LaunchedEffect(posts) {
        listState.scrollToItem(index = 0)
    }
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        items( items = posts, key = {post -> post.postId} ) { post ->
            PostItem(
                name = "${post.firstName} ${post.lastName}",
                username = "@${post.firstName}",
                profileImage = post.profileImg,
                time = convertToRelativeTime(post.createdAt),
                content = post.content,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = if (posts.indexOf(post) == 0) 16.dp else 10.dp,
                        bottom = 10.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                viewModel = sessionViewModel,
                postId =  post.postId,
                initialIsDownvoted = post.isDownvoted
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        if (sessionViewModel.darkMode.value) Color.White
                        else Color.Black
                    ),
            )
        }
    }
}
