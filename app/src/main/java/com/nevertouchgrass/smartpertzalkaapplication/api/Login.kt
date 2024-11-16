package com.nevertouchgrass.smartpertzalkaapplication.api

import com.nevertouchgrass.smartpertzalkaapplication.enums.StatusCodes
import com.nevertouchgrass.smartpertzalkaapplication.http.HttpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class Login {
    companion object {
        fun login(username: String, password: String) : Boolean {
            var response = Pair(0, "")
            GlobalScope.launch(Dispatchers.Main) {
                val data = JSONObject().put(
                    "name", username
                ).put(
                    "password", password
                )
                response = HttpRequest.request("http://192.168.4.2:8080/api/auth/login", "POST", 10_000, data)
            }
            return StatusCodes.OK == StatusCodes.getStatusCode(response.first)
        }
    }
}