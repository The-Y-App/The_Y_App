package com.rrc.adev3007.pixel_perfect.the_y_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.rrc.adev3007.pixel_perfect.the_y_app.session.SessionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sessionViewModel = SessionViewModel(applicationContext)
        if (sessionViewModel.apiKey.value == "undefined") {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        } else {
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        }
        finish()
    }
}
