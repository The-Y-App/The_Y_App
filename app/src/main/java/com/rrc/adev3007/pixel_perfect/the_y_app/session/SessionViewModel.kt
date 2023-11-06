package com.rrc.adev3007.pixel_perfect.the_y_app.session

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.rrc.adev3007.pixel_perfect.the_y_app.components.ScalingLevel

class SessionViewModel(context: Context?) : ViewModel() {
    private val session: Session = Session.getInstance(context)!!

    val apiKey: State<String> = mutableStateOf(session.getString("apiKey", "undefined")!!)
    val email: State<String> = mutableStateOf(session.getString("email","undefined")!!)
    val firstName : State<String> = mutableStateOf(session.getString("firstName", "undefined")!!)
    val lastName : State<String> = mutableStateOf(session.getString("lastName", "undefined")!!)
    val profilePicture : State<String?> = mutableStateOf(session.getString("profilePicture", null))
    val username: State<String> = mutableStateOf(session.getString("username", "undefined")!!)
    val darkMode: State<Boolean> = mutableStateOf(session.getBoolean("darkMode", false))
    val autoplay: State<Boolean> = mutableStateOf(session.getBoolean("autoplay", false))
    val profanityFilter: State<Boolean> = mutableStateOf(session.getBoolean("profanityFilter", false))
    val scale: State<ScalingLevel> = mutableStateOf(
        ScalingLevel.valueOf(
            session.getString("scale", ScalingLevel.Normal.toString())
                ?: ScalingLevel.Normal.toString()
        )
    )

    fun setAPIKey(setAPIKey: String){
        session.putString("apiKey", setAPIKey)
        (apiKey as MutableState<String>).value = setAPIKey
    }
    fun updateEmail(newEmail: String) {
        session.putString("email", newEmail)
        (email as MutableState<String>).value = newEmail
    }

    fun updateFirstName(newFirstName: String){
        session.putString("firstName", newFirstName)
        (firstName as MutableState<String>).value = newFirstName
    }

    fun updateLastName(newLastName : String){
        session.putString("lastName", newLastName)
        (lastName as MutableState<String>).value = newLastName
    }

    fun updateProfilePicture(newProfilePicture : String?){
        session.putString("profilePicture", newProfilePicture?: null)
        (profilePicture as MutableState<String?>).value = newProfilePicture
    }
    fun updateUsername(newUsername: String) {
        session.putString("username", newUsername)
        (username as MutableState<String>).value = newUsername
    }

    fun toggleDarkMode() {
        val currentDarkMode = darkMode.value
        session.putBoolean("darkMode", !currentDarkMode)
        (darkMode as MutableState<Boolean>).value = !currentDarkMode
    }

    fun toggleAutoplay() {
        val currentAutoplay = autoplay.value
        session.putBoolean("autoplay", !currentAutoplay)
        (autoplay as MutableState<Boolean>).value = !currentAutoplay
    }

    fun toggleProfanityFilter() {
        val currentProfanityFilter = profanityFilter.value
        session.putBoolean("profanityFilter", !currentProfanityFilter)
        (profanityFilter as MutableState<Boolean>).value = !currentProfanityFilter
    }

    fun updateScale(newScale: ScalingLevel) {
        session.putString("scale", newScale.toString())
        (scale as MutableState<ScalingLevel>).value = newScale
    }

    fun setProfilePic(picBase64: String){
        session.putString("profilePicture", picBase64)
        (profilePicture as MutableState<String?>).value = picBase64
    }

    fun logOut() {
        session.clearSession()
    }
}
