package com.rrc.adev3007.pixel_perfect.the_y_app.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rrc.adev3007.pixel_perfect.the_y_app.R

@Composable
fun ProfileIcon(
    modifier: Modifier = Modifier,
    iconSize: Dp = 32.dp,
    isDarkMode: Boolean,
    onClick: () -> Unit = {},
    imageBase64: String? = null
) {
    if (imageBase64 != null) {
        val decodedBitmap = decodeBase64Image(imageBase64)
        Image(
            painter = BitmapPainter(decodedBitmap.asImageBitmap()),
            contentDescription = null,
            contentScale = ContentScale.Crop, // You may want to adjust this
            modifier = modifier
                .size(iconSize)
                .clip(CircleShape)
                .clickable { onClick() }
        )
    } else {
        // Display the default image when imageBase64 is null
        Image(
            painter = painterResource(id = R.drawable.person),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = modifier
                .size(iconSize)
                .clip(CircleShape)
                .clickable { onClick() },
            colorFilter = ColorFilter.tint(
                if (isDarkMode) Color.White
                else Color.Black
            )
        )
    }
}

// Function to decode base64 string into Bitmap
fun decodeBase64Image(base64: String): Bitmap {
    val imageBytes = Base64.decode(base64, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}


@Preview
@Composable
fun DefaultProfileIconPreview() {
    ProfileIcon(isDarkMode = true)
}