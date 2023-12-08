package com.rrc.adev3007.pixel_perfect.the_y_app

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rrc.adev3007.pixel_perfect.the_y_app.components.BottomNavBar
import com.rrc.adev3007.pixel_perfect.the_y_app.components.ProfileIcon
import com.rrc.adev3007.pixel_perfect.the_y_app.components.Drawer
import com.rrc.adev3007.pixel_perfect.the_y_app.components.DrawerState
import com.rrc.adev3007.pixel_perfect.the_y_app.components.FloatingCreatePostButton
import com.rrc.adev3007.pixel_perfect.the_y_app.data.Synchronizer
import com.rrc.adev3007.pixel_perfect.the_y_app.data.viewModels.PostViewModel
import com.rrc.adev3007.pixel_perfect.the_y_app.pages.Dislikes
import com.rrc.adev3007.pixel_perfect.the_y_app.pages.Home
import com.rrc.adev3007.pixel_perfect.the_y_app.pages.Search
import com.rrc.adev3007.pixel_perfect.the_y_app.session.Session
import com.rrc.adev3007.pixel_perfect.the_y_app.ui.theme.LogoFontFamily
import com.rrc.adev3007.pixel_perfect.the_y_app.session.SessionViewModel

/*
* Experimental API's used:
* androidx.compose.ui.platform.LocalSoftwareKeyboardController:
*       NewPostForm.kt:62
*       Search.kt:36
*/
class HomeActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val postViewModel = PostViewModel()
        val sessionViewModel = SessionViewModel(applicationContext)
        sessionViewModel.logoutCallback = {
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finish()
        }
        Synchronizer.unauthorizedCallback = {
            Session.getInstance(applicationContext)?.clearSession()
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finish()
        }
        setContent {
            HomeScreen(sessionViewModel, postViewModel)
        }
    }

    @ExperimentalComposeUiApi
    @Composable
    fun HomeScreen(sessionViewModel: SessionViewModel, postViewModel: PostViewModel) {
        val darkMode by sessionViewModel.darkMode
        val scaling by sessionViewModel.scale
        val username by sessionViewModel.username
        val profilePicture by sessionViewModel.profilePicture

        val navController = rememberNavController()
        val currentRoute =
            navController.currentBackStackEntryAsState().value?.destination?.route
        val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = if(darkMode) Color.Black
            else Color.White
        ) {
            Column(
                modifier = if (isLandscape) {
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 25.dp)
                } else {
                    Modifier
                        .fillMaxSize()
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    ProfileIcon(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart),
                        iconSize = 36.dp,
                        onClick = { DrawerState.toggleDrawer() },
                        imageBase64 = profilePicture,
                        isDarkMode = darkMode
                    )
                    Text(
                        text = "y",
                        color = if (darkMode) Color.White else Color.Black,
                        fontSize = 30.sp,
                        fontFamily = LogoFontFamily,
                        modifier =
                        Modifier
                            .align(Alignment.TopCenter)
                            .clickable {
                                if (currentRoute != "Home")
                                    navController.navigate("Home")
                            }
                    )
                }

                Text(
                    text = "Welcome Back $username",
                    color = if(darkMode) Color.White
                    else Color.Black,
                    modifier = Modifier.padding(start = 0.dp)
                )

                Spacer(modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(if (darkMode) Color.White else Color.Black))
                NavHost(
                    navController,
                    startDestination = "Home",
                    modifier = Modifier.weight(1f)
                ) {
                    composable("Home") { Home(postViewModel, sessionViewModel, LocalContext.current) }
                    composable("Search") { Search(postViewModel, sessionViewModel, LocalContext.current) }
                    composable("Dislikes") { Dislikes(postViewModel, sessionViewModel, LocalContext.current) }
                }
                BottomNavBar(
                    navController = navController,
                    currentRoute = currentRoute,
                    modifier = Modifier.padding(horizontal = 30.dp),
                    viewModel = sessionViewModel
                )
            }
            FloatingCreatePostButton(sessionViewModel, postViewModel)
            Drawer(sessionViewModel)
        }
    }
}
