package com.rrc.adev3007.pixel_perfect.the_y_app.session

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrc.adev3007.pixel_perfect.the_y_app.components.DrawerState
import com.rrc.adev3007.pixel_perfect.the_y_app.components.ScalingLevel
import com.rrc.adev3007.pixel_perfect.the_y_app.data.Synchronizer
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.UserAuth
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.UserAuthRequest
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.UserCreate
import kotlinx.coroutines.launch

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
    val loginError: State<String> = mutableStateOf("")
    val registerErrors: State<Map<String, String>> = mutableStateOf(emptyMap())
    var loginCallback: (() -> Unit)? = null
    var logoutCallback: (() -> Unit)? = null

    fun setApiKey(setAPIKey: String){
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
        session.putString("profilePicture", newProfilePicture)
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

    fun setLoginError(error: String) {
        (loginError as MutableState<String>).value = error
    }

    fun setRegisterErrors(errors: Map<String, String>) {
        (registerErrors as MutableState<Map<String, String>>).value = errors
    }

    fun register(formData: UserCreate) {
        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")
        val errors = registerErrors.value.toMutableMap()

        if(formData.username.isBlank()) errors["username"] = "Username is required"
        if (formData.email.isEmpty()) errors["email"] = "Email is required"
        else if (!emailRegex.matches(formData.email)) errors["email"] = "Invalid email"
        if (formData.firstName.isBlank()) errors["firstName"] = "First name is required"
        if (formData.lastName.isBlank()) errors["lastName"] = "Last name is required"
        if (formData.password.isBlank()) errors["password"] = "Password is required"
        setRegisterErrors(errors)

        if (registerErrors.value.isEmpty()) {
            viewModelScope.launch {
                val response = Synchronizer.api.postUser(formData)
                if (response.isSuccessful) {
                    logIn(
                        UserAuth(
                            formData.username,
                            formData.password
                        )
                    )
                } else {
                    if (response.code() == 409) {
                        val updatedErrors = registerErrors.value.toMutableMap()
                        updatedErrors["username"] = "Username already in use"
                        setRegisterErrors(updatedErrors)
                    }

                    if(response.code() == 416){
                        val updatedErrors = registerErrors.value.toMutableMap()
                        updatedErrors["email"] = "Email already in use"
                        setRegisterErrors(updatedErrors)
                    }

                    if (response.code() == 400) {
                        val updatedErrors = registerErrors.value.toMutableMap()
                        updatedErrors["fields"] = "Required Fields are Missing"
                        setRegisterErrors(updatedErrors)
                    }
                }
            }
        }
    }

    fun logIn(userAuth: UserAuth) {
        viewModelScope.launch {
            val response = Synchronizer.api.postLogin(userAuth)
            if (response.isSuccessful) {
                val userAccount = response.body()
                setApiKey(userAccount?.apiKey.toString())
                updateUsername(userAuth.username)
                updateEmail(userAccount?.email.toString())
                updateFirstName(userAccount?.firstName.toString())
                updateLastName(userAccount?.lastName.toString())
                updateScale((ScalingLevel.valueOf(userAccount?.uiScale.toString())))
                updateProfilePicture(userAccount?.profilePicture)
                loginCallback?.invoke()
            } else {
                setLoginError("Username or password is incorrect!")
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            val response = Synchronizer.api.postLogout(UserAuthRequest(apiKey.value, username.value))
            if (response.isSuccessful) {
                session.clearSession()
                DrawerState.toggleDrawer()
                logoutCallback?.invoke()
            }
        }
    }
}
