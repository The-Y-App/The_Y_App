package com.rrc.adev3007.pixel_perfect.the_y_app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rrc.adev3007.pixel_perfect.the_y_app.R
import com.rrc.adev3007.pixel_perfect.the_y_app.session.SessionViewModel

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    currentRoute: String? = "Home",
    viewModel: SessionViewModel
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if (viewModel.darkMode.value) Color.Black
                else Color.White
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                iconRes = R.drawable.home,
                onClick = {
                    navController.navigate("Home")
                },
                isSelected = currentRoute == "Home",
                contentDescription = currentRoute,
                isDarkMode = viewModel.darkMode.value
            )
            BottomNavItem(
                iconRes = R.drawable.search,
                onClick = {
                    navController.navigate("Search")
                },
                isSelected = currentRoute == "Search",
                contentDescription = currentRoute,
                isDarkMode = viewModel.darkMode.value
            )
            BottomNavItem(
                iconRes = R.drawable.thumbs_down,
                onClick = {
                    navController.navigate("Dislikes")
                },
                isSelected = currentRoute == "Dislikes",
                contentDescription = currentRoute,
                isDarkMode = viewModel.darkMode.value
            )
        }
    }
}

@Composable
fun BottomNavItem(
    iconRes: Int,
    onClick: () -> Unit,
    isSelected: Boolean,
    contentDescription: String?,
    isDarkMode: Boolean,
) {
    IconToggleButton(
        checked = isSelected,
        onCheckedChange = { if (!isSelected) onClick() },
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = "$contentDescription Icon",
            tint =
                if (isSelected)
                    if (isDarkMode) Color.White
                    else Color.Black
                else Color.Gray
        )
    }
}
