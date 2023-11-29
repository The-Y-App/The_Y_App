package com.rrc.adev3007.pixel_perfect.the_y_app.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.rrc.adev3007.pixel_perfect.the_y_app.data.Synchronizer
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.CreatePostRequest
import com.rrc.adev3007.pixel_perfect.the_y_app.data.viewModels.PostViewModel
import com.rrc.adev3007.pixel_perfect.the_y_app.session.SessionViewModel
import kotlinx.coroutines.launch

object NewPostFormState {
    var isFormOpen by mutableStateOf(false)

    fun toggleForm() {
        isFormOpen = !isFormOpen
    }
}

@ExperimentalComposeUiApi
@Composable
fun NewPostForm(sessionViewModel: SessionViewModel, postViewModel: PostViewModel) {
    val isFormOpen = NewPostFormState.isFormOpen
    var text by remember { mutableStateOf("") }
    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    DisposableEffect(isFormOpen) {
        if (isFormOpen) {
            focusRequester.requestFocus()
        } else {
            keyboardController?.hide()
        }

        onDispose {
            if (isFormOpen) {
                text = ""
                focusRequester.freeFocus()
            }
        }
    }
    AnimatedVisibility(
        visible = isFormOpen,
        enter = fadeIn(animationSpec = tween(600)),
        exit = fadeOut(animationSpec = tween(600)),
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .clickable(false, onClick = {})
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cancel",
                        color = Color.White,
                        modifier = Modifier
                            .clickable {
                                keyboardController?.hide()
                                NewPostFormState.toggleForm()
                            }
                    )
                    Box(modifier = Modifier
                        .background(
                            color = if (text == "") Color(0xFF3385FF) else Color.Blue,
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            if (text != "") {
                                coroutineScope.launch {
                                    if (!postViewModel.isFetchInProgress.value) {
                                        val response = Synchronizer.api {
                                            createPost(
                                                CreatePostRequest(
                                                    sessionViewModel.apiKey.value,
                                                    sessionViewModel.username.value,
                                                    text,
                                                    null
                                                )
                                            )
                                        }
                                        if (response != null) {
                                            if (response.isSuccessful) {
                                                postViewModel.getHomePosts(
                                                    sessionViewModel.username.value,
                                                    sessionViewModel.apiKey.value
                                                )
                                            }
                                        } else {
                                            Toast.makeText(
                                                localContext,
                                                "Failed to create Post!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                        keyboardController?.hide()
                                        NewPostFormState.toggleForm()
                                    }
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Post",
                            color = Color.White,
                            modifier = Modifier
                                .padding(vertical =  5.dp, horizontal = 15.dp))
                    }
                }
                    Row(
                        modifier = Modifier
                            .padding(top = 5.dp)
                    ) {
                        ProfileIcon(
                            imageBase64 = sessionViewModel.profilePicture.value,
                            iconSize = 28.dp,
                            modifier = Modifier.padding(bottom = 35.dp, end = 10.dp),
                            isDarkMode = sessionViewModel.darkMode.value
                        )
                        BasicTextField(
                            value = text,
                            onValueChange = { newText ->
                                text = newText
                            },
                            cursorBrush = SolidColor(Color.White),
                            decorationBox = { innerTextField ->
                                if (text == "") {
                                    Text(
                                        text = "What's happening?",
                                        color = Color.White,
                                        fontSize = 15.sp
                                    )
                                }
                                innerTextField()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .clickable { focusRequester.requestFocus() },
                            textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                        )
                    }
                }
            }
        }
    }
