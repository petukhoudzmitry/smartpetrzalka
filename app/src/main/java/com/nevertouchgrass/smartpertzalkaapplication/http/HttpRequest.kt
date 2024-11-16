package com.nevertouchgrass.smartpertzalkaapplication.http

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
    }
}