package com.rrc.adev3007.pixel_perfect.the_y_app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rrc.adev3007.pixel_perfect.the_y_app.R
import com.rrc.adev3007.pixel_perfect.the_y_app.data.viewModels.PostViewModel
import com.rrc.adev3007.pixel_perfect.the_y_app.session.SessionViewModel

@Composable
fun PostItem(
    name: String,
    username: String,
    profileImage: String?,
    time: String,
    content: String,
    modifier: Modifier,
    viewModel: SessionViewModel,
    postViewModel: PostViewModel,
    postId: Int,
    initialIsDownvoted: Boolean
) {
    val darkMode by viewModel.darkMode
    val profanityFilter by viewModel.profanityFilter
    val scale by viewModel.scale
    var isDownvoted by rememberSaveable { mutableStateOf(initialIsDownvoted) }

    Box(
        modifier
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    when (scale) {
                        ScalingLevel.Small -> 7.dp
                        ScalingLevel.Normal -> 10.dp
                        else -> 13.dp
                    }
                )
            ) {
                ProfileIcon(imageBase64 = profileImage, isDarkMode = viewModel.darkMode.value)
                Column (modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            when (scale) {
                                ScalingLevel.Small -> 3.dp
                                ScalingLevel.Normal -> 5.dp
                                else -> 7.dp
                            }
                        )
                    ) {
                        Text(
                            text = name,
                            color = if (darkMode) Color.White else Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = when (scale) {
                                ScalingLevel.Small -> 10.sp
                                ScalingLevel.Normal -> 12.sp
                                else -> 14.sp
                            }
                        )

                        Text(
                            text = "$username · $time",
                            color = if (darkMode) Color.White else Color.Black,
                            fontSize = when (scale) {
                                ScalingLevel.Small -> 9.sp
                                ScalingLevel.Normal -> 10.sp
                                else -> 11.sp
                            }
                        )
                    }

                    Text(
                        text = content,
                        color = if (darkMode) Color.White else Color.Black,
                        fontSize = when (scale) {
                            ScalingLevel.Small -> 9.sp
                            ScalingLevel.Normal -> 11.sp
                            else -> 13.sp
                        }
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    when (scale) {
                        ScalingLevel.Small -> 7.dp
                        ScalingLevel.Normal -> 10.dp
                        else -> 13.dp
                    }),
                verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(
                            top = when (scale) {
                                ScalingLevel.Small -> 7.dp
                                ScalingLevel.Normal -> 10.dp
                                else -> 13.dp
                            }
                        ),
            ) {
                // Downvote Arrow - Default
                    Image(
                        painter = if(isDownvoted) painterResource(id = R.drawable.default_downvote_filled) else painterResource(id = R.drawable.default_downvote),
                        contentDescription = null,
                        colorFilter = if(viewModel.darkMode.value) ColorFilter.tint(Color.White) else ColorFilter.tint(Color.Black),
                        modifier = Modifier
                            .size(
                                width = 18.dp + when (scale) {
                                    ScalingLevel.Small -> 7.dp
                                    ScalingLevel.Normal -> 10.dp
                                    else -> 13.dp
                                },
                                height = 18.dp + when (scale) {
                                    ScalingLevel.Small -> 7.dp
                                    ScalingLevel.Normal -> 10.dp
                                    else -> 13.dp
                                }
                            )
                            .clickable {
                                isDownvoted = if(!isDownvoted) {
                                    postViewModel.downVote(
                                        postId,
                                        viewModel.username.value,
                                        viewModel.apiKey.value
                                    )
                                    true
                                } else {
                                    postViewModel.deleteDownvote(
                                        postId,
                                        viewModel.username.value,
                                        viewModel.apiKey.value
                                    )
                                    false
                                }
                            }
                    )
//
//                // Upvote ARROW - Default
//                Image(
//                    painter = painterResource(id = R.drawable.default_downvote),
//                    contentDescription = null,
//                    colorFilter = if(viewModel.darkMode.value) ColorFilter.tint(Color.White) else ColorFilter.tint(Color.Black),
//                    modifier = Modifier
//                        .size(
//                            width = 18.dp + when (scale) {
//                                ScalingLevel.Small -> 7.dp
//                                ScalingLevel.Normal -> 10.dp
//                                else -> 13.dp
//                            },
//                            height = 18.dp + when (scale) {
//                                ScalingLevel.Small -> 7.dp
//                                ScalingLevel.Normal -> 10.dp
//                                else -> 13.dp
//                            }
//                        )
//                        .graphicsLayer {
//                            this.rotationZ = 180f
//                        }
//                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewPost() {
    PostItem(
        name = "Sample Post",
        username = "SampleUser",
        profileImage = null,
        time = "2h",
        content = "This is the post content",
        modifier = Modifier,
        viewModel = SessionViewModel(null),
        postViewModel = PostViewModel(),
        postId = 1,
        initialIsDownvoted = true
    )
}