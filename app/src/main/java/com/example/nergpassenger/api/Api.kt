package com.example.nergpassenger.api

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.nergpassenger.AlertHelper
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class Api {
    companion object {
        fun register(username: String, password: String): Int {
            val url = URL("http://10.0.2.2:3000/auth/register")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = true
            httpURLConnection.setRequestProperty("Accept", "application/json")
            httpURLConnection.setRequestProperty("Content-Type", "application/json")
            val body = JSONObject()
            body.put("username", username)
            body.put("password", password)

            val outputStream = OutputStreamWriter(httpURLConnection.outputStream)
            outputStream.write(body.toString())
            outputStream.flush()

            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_CREATED)
                return 1
            if (httpURLConnection.responseMessage == "Username is already taken")
                return 0
            return 0
        }

        fun login(username: String, password: String): String {
            var response = ""
            val url = URL("http://10.0.2.2:3000/auth/login")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = true
            httpURLConnection.setRequestProperty("Accept", "application/json")
            httpURLConnection.setRequestProperty("Content-Type", "application/json")
            val body = JSONObject()
            body.put("username", username)
            body.put("password", password)

            val outputStream = OutputStreamWriter(httpURLConnection.outputStream)
            outputStream.write(body.toString())
            outputStream.flush()

            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                response = httpURLConnection.inputStream.bufferedReader(
                    StandardCharsets
                        .UTF_8
                ).use {
                    it.readText()
                }
                return response
            } else {
                return "INVALID"
            }
        }

        fun getUserDetails(token: String, context: Context): String {
            var response = ""
            val url = URL("http://10.0.2.2:3000/users/me")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = false
            httpURLConnection.setRequestProperty("Accept", "application/json")
            httpURLConnection.setRequestProperty("Content-Type", "application/json")
            httpURLConnection.setRequestProperty("Authorization", "Bearer $token")

            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                response = httpURLConnection.inputStream.bufferedReader(
                    StandardCharsets
                        .UTF_8
                ).use {
                    it.readText()
                }
                Log.i("response", response.toString())
                return response
            } else {
                Handler(Looper.getMainLooper()).post {
                    Log.i("response", httpURLConnection.responseCode.toString())
                    AlertHelper.showAlert(
                        context,
                        "Error No data",
                        httpURLConnection.responseMessage
                    )
                }
            }
            return response
        }

        fun updatePersonalDetails(
            givenName: String, familyName: String, birthdate: String,
            token: String, context: Context
        ): String {
            var response = ""
            val url = URL("http://10.0.2.2:3000/users/me/personal-info")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "PATCH"
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = true
            httpURLConnection.setRequestProperty("Accept", "application/json")
            httpURLConnection.setRequestProperty("Content-Type", "application/json")
            httpURLConnection.setRequestProperty("Authorization", "Bearer $token")
            val body = JSONObject()
            body.put("givenName", givenName)
            body.put("familyName", familyName)
            body.put("birthdate", birthdate)

            val outputStream = OutputStreamWriter(httpURLConnection.outputStream)
            outputStream.write(body.toString())
            outputStream.flush()

            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                response = httpURLConnection.inputStream.bufferedReader(
                    StandardCharsets
                        .UTF_8
                ).use {
                    it.readText()
                }
                Handler(Looper.getMainLooper()).post {
                    Log.i("response", httpURLConnection.responseCode.toString())
                    AlertHelper.showAlert(context, "Success", httpURLConnection.responseMessage)
                }
                return response
            } else {
                Handler(Looper.getMainLooper()).post {
                    Log.i("response", httpURLConnection.responseCode.toString())
                    AlertHelper.showAlert(context, "Error", httpURLConnection.responseMessage)
                }
            }
            return response
        }

        fun updatePaymentDetails(
            cardType: String, cardNumber: String, cardExpiration: String, cardSecurityCode: String,
            token: String, context: Context
        ): String {
            var response = ""
            val url = URL("http://10.0.2.2:3000/users/me/payment-info")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "PATCH"
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = true
            httpURLConnection.setRequestProperty("Accept", "application/json")
            httpURLConnection.setRequestProperty("Content-Type", "application/json")
            httpURLConnection.setRequestProperty("Authorization", "Bearer $token")
            val body = JSONObject()
            body.put("cardType", cardType)
            body.put("cardNumber", cardNumber)
            body.put("cardExpiration", cardExpiration)
            body.put("cardSecurityCode", cardSecurityCode)

            val outputStream = OutputStreamWriter(httpURLConnection.outputStream)
            outputStream.write(body.toString())
            outputStream.flush()

            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                response = httpURLConnection.inputStream.bufferedReader(
                    StandardCharsets
                        .UTF_8
                ).use {
                    it.readText()
                }
                Handler(Looper.getMainLooper()).post {
                    Log.i("response", httpURLConnection.responseCode.toString())
                    AlertHelper.showAlert(context, "Success", httpURLConnection.responseMessage)
                }
                return response
            } else {
                Handler(Looper.getMainLooper()).post {
                    val builder = AlertDialog.Builder(context)
                    AlertHelper.showAlert(context, "Error", httpURLConnection.responseMessage)
                }
            }
            return response
        }

        fun getUserTickets(token: String, context: Context): String {
            var response = ""
            val url = URL("http://10.0.2.2:3000/users/me/tickets")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = false
            httpURLConnection.setRequestProperty("Accept", "application/json")
            httpURLConnection.setRequestProperty("Content-Type", "application/json")
            httpURLConnection.setRequestProperty("Authorization", "Bearer $token")

            val responseCode = httpURLConnection.responseCode
            Log.i("RESPONSE", responseCode.toString())
            if (responseCode == HttpURLConnection.HTTP_OK) {
                response = httpURLConnection.inputStream.bufferedReader(
                    StandardCharsets
                        .UTF_8
                ).use {
                    it.readText()
                }
                return response
            } else {
                Handler(Looper.getMainLooper()).post {
                    AlertHelper.showAlert(context, "Error", httpURLConnection.responseMessage)
                }
            }
            return response
        }
    }
}