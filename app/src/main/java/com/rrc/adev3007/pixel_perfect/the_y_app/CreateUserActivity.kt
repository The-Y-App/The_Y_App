package com.rrc.adev3007.pixel_perfect.the_y_app

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rrc.adev3007.pixel_perfect.the_y_app.data.Synchronizer
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.UserAuth
import com.rrc.adev3007.pixel_perfect.the_y_app.data.models.UserCreate
import com.rrc.adev3007.pixel_perfect.the_y_app.session.SessionViewModel
import kotlinx.coroutines.launch


class CreateUserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = SessionViewModel(applicationContext)
        setContent {
            CreateUserScreen(viewModel)
        }
    }
}

@Composable
fun CreateUserScreen(viewModel: SessionViewModel) {
    var passwordVisability by remember {
        mutableStateOf(true)
    }
    val activity = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()

    val userViewModel = remember { CreateUserViewModel() }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())

        /*
        .background(Color(0xffF0F0F0))
            TODO? Find fix for textfields background color being outside of it's outline
        */
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null
        )
        Text(text = "Welcome To Y", fontSize = 24.sp)
        Spacer(modifier = Modifier.padding(bottom = 24.dp))
        if(userViewModel.errors.isNotEmpty()) ErrorMessages(userViewModel.errors)
        OutlinedTextField(
            value = userViewModel.username,
            label = { Text(text = "Username") },
            onValueChange = {
                userViewModel.username = it
                val updatedErrors = userViewModel.errors.toMutableMap()
                updatedErrors.remove("username")
                userViewModel.errors = updatedErrors
            },
            placeholder = { Text(text = "Enter Username") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = userViewModel.email,
            label = { Text(text = "Email") },
            onValueChange = {
                userViewModel.email = it
                val updatedErrors = userViewModel.errors.toMutableMap()
                updatedErrors.remove("email")
                userViewModel.errors = updatedErrors
                            },
            placeholder = { Text(text = "Enter Email") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = userViewModel.firstName,
            label = { Text(text = "First Name") },
            onValueChange = {
                userViewModel.firstName = it
                val updatedErrors = userViewModel.errors.toMutableMap()
                updatedErrors.remove("firstName")
                userViewModel.errors = updatedErrors},
            placeholder = { Text(text = "Enter First Name") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = userViewModel.lastName,
            label = { Text(text = "Last Name") },
            onValueChange = {
                userViewModel.lastName = it
                val updatedErrors = userViewModel.errors.toMutableMap()
                updatedErrors.remove("lastName")
                userViewModel.errors = updatedErrors},
            placeholder = { Text(text = "Enter Last Name") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = userViewModel.password,
            label = { Text(text = "Password") },
            onValueChange = {
                userViewModel.password = it
                val updatedErrors = userViewModel.errors.toMutableMap()
                updatedErrors.remove("password")
                userViewModel.errors = updatedErrors},
            placeholder = { Text(text = "Enter Password") },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisability)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            trailingIcon = {
                if (passwordVisability) {
                    IconButton(
                        onClick = {
                            passwordVisability = false
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.visibility_off),
                            contentDescription = null
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            passwordVisability = true
                        },
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.visibility_on),
                            contentDescription = null
                        )
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        )

        Spacer(modifier = Modifier.padding(bottom = 46.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Button(
                onClick = {
                    try {
                        if(userViewModel.validateForm()) {
                            coroutineScope.launch {
                                val user = UserCreate(
                                    username = userViewModel.username,
                                    email = userViewModel.email,
                                    firstName = userViewModel.firstName,
                                    lastName = userViewModel.lastName,
                                    password = userViewModel.password
                                )
                                val response = Synchronizer.api.postUser(user)
                                if (response.isSuccessful) {
                                    val createdUser = UserAuth(userViewModel.username, userViewModel.password)
                                    val navigate = Intent(activity, LoginActivity::class.java)
                                    navigate.putExtra("CreatedUser", createdUser)
                                    activity.finish()
                                    activity.startActivity(navigate)
                                } else {
                                    if (response.code() == 409) {
                                        val updatedErrors = userViewModel.errors.toMutableMap()
                                        updatedErrors["username"] = "Username already in use"
                                        userViewModel.errors = updatedErrors
                                    }

                                    if(response.code() == 416){
                                        Log.i("loginError", "hello world")
                                        val updatedErrors = userViewModel.errors.toMutableMap()
                                        updatedErrors["email"] = "Email already in use"
                                        userViewModel.errors = updatedErrors
                                    }

                                    if (response.code() == 400) {
                                        val updatedErrors = userViewModel.errors.toMutableMap()
                                        updatedErrors["fields"] = "Required Fields are Missing"
                                        userViewModel.errors = updatedErrors
                                    }
                                    Log.e("loginError", response.code().toString())
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Login failed: ${e.message}")
                    }
                },
                enabled = userViewModel.errors.isEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Sign Up", Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            ) {
                // Solid line on the left
                Divider(
                    modifier = Modifier
                        .height(1.dp)
                        .weight(1f)
                        .background(Color.Black)
                )

                // Some text
                Text(
                    text = "or",
                    Modifier.padding(horizontal = 18.dp)
                )

                // Solid line on the right
                Divider(
                    modifier = Modifier
                        .height(1.dp)
                        .weight(1f)
                        .background(Color.Black)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                onClick = {
                    activity.finish()
                    activity.startActivity(Intent(activity, LoginActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Login", Modifier.padding(8.dp))
            }
        }
    }
}
@Composable
fun ErrorMessages(errors: Map<String, String>) {
    Column {
        errors.forEach { (key, error) ->
            Text(
                text = error,
                color = Color.Red,
                fontSize = 16.sp // Adjust font size as needed
            )
        }
    }
}
