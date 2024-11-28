package com.nevertouchgrass.smartpertzalkaapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.auth0.jwt.JWT
import com.nevertouchgrass.smartpertzalkaapplication.activities.LoginScreen
import com.nevertouchgrass.smartpertzalkaapplication.db.AppDatabase
import com.nevertouchgrass.smartpertzalkaapplication.http.HttpRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.util.Date

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private lateinit var applicationDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applicationDatabase =  Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app-database").build()

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "checking_token") {
                composable("checking_token") {
                    CheckAuthentication(navController)
                }
                composable("login") {
                    LoginScreen(applicationDatabase, onLoginSuccess = {
                        navController.navigate("home")
                    })
                }
                composable("home") {
                    CoroutineScope(Dispatchers.IO).launch {
                        withContext(Dispatchers.IO) {
                        }
                    }
                    HomePage()
                }
            }
        }

    }


    @Composable
    fun QRCode(uuid: String) {

        val qrCode = remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    val jwt = applicationDatabase.userDao().getAll().first().token
                    qrCode.value = HttpRequest.postRequestWithJwt("http://192.168.4.2:8080/api/reservation/get-qr", jwt, uuid, 10_000).second
                }
            }
        }

        val decodedBytes = Base64.decode(qrCode.value, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(text = "Scan QR-Code to enter the playground", modifier = Modifier.align(Alignment.CenterHorizontally), style = MaterialTheme.typography.headlineMedium)
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = "QRCode", modifier = Modifier
                .padding(16.dp)
                .fillMaxSize())
        }
    }


    @Composable
    fun CheckAuthentication(navController: NavController) {
        lifecycleScope.launch {
            val users = withContext(Dispatchers.IO) {
                applicationDatabase.userDao().getAll()
            }

            if (users.isEmpty()) {
                navController.navigate("login")
            } else {
                val user = users.first()
                if (JWT.decode(user.token).expiresAt.after(Date()).not()) {
                    applicationDatabase.userDao().delete(user)
                    navController.navigate("login")
                } else {
                    navController.navigate("home")
                }
            }
        }
    }
    @Composable
    fun HomePage() {

        val response = remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                val jwt = applicationDatabase.userDao().getAll().first().token
                response.value = HttpRequest.getRequestWithJwt("http://192.168.4.2:8080/api/reservation/get-reservations", jwt, 10_000).second
            }
        }

        Box(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            if (response.value.isNotEmpty()) {
                LazyColumn {
                    val array = JSONArray(response.value)
                    items(array.length()) { i ->
                        Card(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Column {
                                Text(text = "Reservation", modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.CenterHorizontally), style = MaterialTheme.typography.headlineMedium)

                                OutlinedTextField(value = array.getJSONObject(i).getString("day"), onValueChange = {},
                                    Modifier
                                        .focusable(false)
                                        .clickable {}
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .align(Alignment.CenterHorizontally), label = {
                                        Text("Date")
                                    })

                                OutlinedTextField(value = array.getJSONObject(i).getString("playgroundName"), onValueChange = {},
                                    Modifier
                                        .focusable(false)
                                        .clickable {}
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .align(Alignment.CenterHorizontally), label = {
                                        Text("Playground")
                                    })

                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
                                    ) {
                                        OutlinedTextField(value = array.getJSONObject(i).getString("startTime"), onValueChange = {},
                                            Modifier
                                                .focusable(false)
                                                .clickable {}
                                                .padding(16.dp)
                                                .fillMaxWidth()
                                                .align(Alignment.Start), label = {
                                                Text("Start")
                                            })
                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
                                    ) {
                                        OutlinedTextField(value = array.getJSONObject(i).getString("endTime"), onValueChange = {},
                                            Modifier
                                                .focusable(false)
                                                .clickable {}
                                                .padding(16.dp)
                                                .fillMaxWidth()
                                                .align(Alignment.End), label = {
                                                Text("End time")
                                            })
                                    }
                                }
                                val showQRCode = remember { mutableStateOf(false) }
                                Button(onClick = {
                                    showQRCode.value = true
                                }, modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally)) {
                                    Text("Enter")
                                }
                                if (showQRCode.value) {
                                    QRCode(uuid = array.getJSONObject(i).getString("uuid"))
                                }
                            }
                        }
                    }
                }
            } else {
                Text(text = "No reservations available")
            }

        }

    }
}

