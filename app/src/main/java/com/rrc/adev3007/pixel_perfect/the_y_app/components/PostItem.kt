package com.rrc.adev3007.pixel_perfect.the_y_app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rrc.adev3007.pixel_perfect.the_y_app.R
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
    postId: Int
) {
    val darkMode by viewModel.darkMode
    val profanityFilter by viewModel.profanityFilter
    val scale by viewModel.scale

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
                Image(
                    painter = painterResource(id = R.drawable.default_downvote),
                    contentDescription = null,
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
                            viewModel.downVote(postId)
                    }
                )

                Image(
                    painter = painterResource(id = R.drawable.default_downvote),
                    contentDescription = null,
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
                        .graphicsLayer {
                            this.rotationZ = 180f
                        }
                )
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
        postId = 1
    )
}