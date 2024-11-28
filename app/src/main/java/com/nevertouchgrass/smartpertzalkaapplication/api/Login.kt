package com.nevertouchgrass.smartpertzalkaapplication.api

import android.util.Log
import com.nevertouchgrass.smartpertzalkaapplication.http.HttpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Login {
    companion object {
        suspend fun login(username: String, password: String) : String {
            return withContext(Dispatchers.IO) {
                val data = JSONObject().put(
                    "email", username
                ).put(
                    "password", password
                )
                return@withContext HttpRequest.request("http://192.168.4.2:8080/api/auth/login", "POST", 10_000, data).second
            }
        }
    }
}