package com.rrc.adev3007.pixel_perfect.the_y_app.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.rrc.adev3007.pixel_perfect.the_y_app.components.PostItem
import com.rrc.adev3007.pixel_perfect.the_y_app.data.viewModels.PostViewModel
import com.rrc.adev3007.pixel_perfect.the_y_app.helpers.convertToRelativeTime
import com.rrc.adev3007.pixel_perfect.the_y_app.session.SessionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalComposeUiApi
@Composable
fun Search(viewModel: PostViewModel, sessionViewModel: SessionViewModel, context: Context) {
    val posts = viewModel.searchedPosts.value
    val query = viewModel.searchQuery
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = query.value,
            onValueChange = {
                query.value = it
            },
            label = {
                Text(
                    "Search",
                    color =
                        if (sessionViewModel.darkMode.value) Color.White
                        else Color.Black
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint =
                        if (sessionViewModel.darkMode.value) Color.White
                        else Color.Black
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    var isSuccessful: Boolean
                    if (query.value != "" && !viewModel.isFetchInProgress.value) {
                        keyboardController?.hide()
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                isSuccessful = viewModel.search(
                                    sessionViewModel.username.value,
                                    sessionViewModel.apiKey.value
                                )
                            }
                            if (!isSuccessful && posts.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "There appears to be a network error, please try again later",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor =
                    if (sessionViewModel.darkMode.value) Color.White
                    else Color.Black,
                focusedPlaceholderColor =
                    if (sessionViewModel.darkMode.value) Color.White
                    else Color.Black,
                focusedTextColor =
                    if (sessionViewModel.darkMode.value) Color.White
                    else Color.Black,
                unfocusedTextColor =
                    if (sessionViewModel.darkMode.value) Color.White
                    else Color.Black,
                unfocusedBorderColor =
                    if (sessionViewModel.darkMode.value) Color.White
                    else Color.Black,
                focusedBorderColor = Color.Blue
            )
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(posts.size) { index ->
                PostItem(
                    name = "${posts[index].firstName} ${posts[index].lastName}",
                    username = "@${posts[index].firstName}",
                    profileImage = posts[index].profileImg,
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
                    viewModel = sessionViewModel,
                    postViewModel = viewModel,
                    postId = posts[index].postId,
                    initialIsDownvoted = posts[index].isDownvoted
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
}
