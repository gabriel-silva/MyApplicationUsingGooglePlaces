package com.myapplicationusinggoogleplaces.service

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpConnection {

    companion object {

        fun getData(url: URL): String {

            var urlConnection: HttpURLConnection? = null
            val stringBuilder = StringBuilder()
            try {
                urlConnection = url.openConnection() as HttpURLConnection

                val responseCode = urlConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val stream = BufferedInputStream(urlConnection.inputStream)
                    val bufferedReader = BufferedReader(InputStreamReader(stream))
                    bufferedReader.forEachLine { stringBuilder.append(it) }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()
            }

            return stringBuilder.toString()
        }

    }

}