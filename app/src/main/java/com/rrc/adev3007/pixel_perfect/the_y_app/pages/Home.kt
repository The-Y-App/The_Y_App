package com.rrc.adev3007.pixel_perfect.the_y_app.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
    LaunchedEffect(Unit) {
        if (posts.isEmpty()) {
            viewModel.getHomePosts(sessionViewModel.username.value, sessionViewModel.apiKey.value)
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(posts.size) { index ->
            PostItem(
                name = "${posts[index].firstName} ${posts[index].lastName}",
                username = "@${posts[index].firstName}",
                profileImage = posts[index].media,
                time = convertToRelativeTime(posts[index].createdAt),
                content = posts[index].content,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = if (index == 0) 16.dp else 10.dp,
                        bottom = 10.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                viewModel = sessionViewModel
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
