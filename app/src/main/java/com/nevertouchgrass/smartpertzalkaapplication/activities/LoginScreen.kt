package com.nevertouchgrass.smartpertzalkaapplication.activities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevertouchgrass.smartpertzalkaapplication.api.Login
import com.nevertouchgrass.smartpertzalkaapplication.db.AppDatabase
import com.nevertouchgrass.smartpertzalkaapplication.db.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun LoginScreen(applicationDatabase: AppDatabase, onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = username,
            label = {
                Text(text = "Username", modifier = Modifier.padding(start = 8.dp))
            },
            onValueChange = { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            label = {
                Text(text = "Username", modifier = Modifier.padding(start = 8.dp))
            },
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(onClick = {
            if (username.isBlank() || password.isBlank()) {
                errorMessage = "Please enter both username and password."
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    val response = Login.login(username, password)
                    if (response.isNotEmpty()) {
                        errorMessage =  ""
                        CoroutineScope(Dispatchers.IO).launch {
                            applicationDatabase.userDao().insertAll(User(response))
                        }
                        onLoginSuccess()
                    } else {
                        errorMessage = "Invalid login data."
                    }
                }

            }
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Login")
        }
    }
}