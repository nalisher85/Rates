package com.example.rates.currency_rates.data.network

import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject

data class Error(
    val code: String = "",
    val message: String = ""
)

fun ResponseBody.toError(): Error? {
    val gson = Gson( )
    val jsonObject = try {
        JSONObject(string())
    } catch (e: Exception) {
        null
     }
    return gson.fromJson(jsonObject.toString(), Error::class.java)
}