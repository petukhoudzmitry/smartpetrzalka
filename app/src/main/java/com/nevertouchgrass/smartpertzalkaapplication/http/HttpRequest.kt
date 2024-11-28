package com.nevertouchgrass.smartpertzalkaapplication.http

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class HttpRequest {
    companion object {
        suspend fun request(urlString: String, method: String, timeout: Int, data: JSONObject): Pair<Int, String> {
            return withContext(Dispatchers.IO) {
                var response = ""
                var responseCode = 0
                try {
                    val url = URL(urlString)
                    val urlConnection = url.openConnection() as HttpURLConnection
                    urlConnection.requestMethod = method
                    urlConnection.connectTimeout = timeout
                    urlConnection.readTimeout = timeout
                    urlConnection.setRequestProperty("Content-Type", "application/json")
                    urlConnection.setRequestProperty("Accept", "application/json")
                    urlConnection.doOutput = true

                    val outputStream = urlConnection.outputStream

                    outputStream.write(data.toString().toByteArray())
                    outputStream.flush()
                    outputStream.close()

                    responseCode = urlConnection.responseCode
                    response = urlConnection.inputStream.bufferedReader().readText()
                } catch (ignore: IOException) {}
                return@withContext Pair(responseCode, response)
            }
        }

        suspend fun getRequestWithJwt(urlString: String, jwt: String, timeout: Int): Pair<Int, String> {
            return withContext(Dispatchers.IO) {
                var response = ""
                var responseCode = 0
                try {
                    val url = URL(urlString)
                    val urlConnection = url.openConnection() as HttpURLConnection
                    urlConnection.connectTimeout = timeout
                    urlConnection.readTimeout = timeout
                    urlConnection.setRequestProperty(
                        "Authorization", "Bearer $jwt"
                    )
                    urlConnection.requestMethod = "GET"

                    responseCode = urlConnection.responseCode
                    response = urlConnection.inputStream.bufferedReader().readText()
                } catch (ignore: IOException) {}
                return@withContext Pair(responseCode, response)
            }
        }

        suspend fun postRequestWithJwt(urlString: String, jwt: String, data: String, timeout: Int): Pair<Int, String> {
            return withContext(Dispatchers.IO) {
                var response = ""
                var responseCode = 0
                try {
                    val url = URL(urlString)
                    val urlConnection = url.openConnection() as HttpURLConnection
                    urlConnection.connectTimeout = timeout
                    urlConnection.readTimeout = timeout
                    urlConnection.setRequestProperty(
                        "Authorization", "Bearer $jwt"
                    )
                    urlConnection.doOutput = true
                    urlConnection.requestMethod = "POST"

                    val output = urlConnection.outputStream
                    output.write(data.toByteArray())
                    output.flush()
                    output.close()

                    responseCode = urlConnection.responseCode
                    response = urlConnection.inputStream.bufferedReader().readText()
                } catch (ignore: IOException) {}
                return@withContext Pair(responseCode, response)
            }
        }
    }
}