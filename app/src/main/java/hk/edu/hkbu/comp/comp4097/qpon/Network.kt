package hk.edu.hkbu.comp.comp4097.qpon

import android.content.Context
import android.util.Log
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import hk.edu.hkbu.comp.comp4097.qpon.ui.user.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object Network {
    var result:String = ""

    fun getTextFromNetwork(url: String): String {
        val builder = StringBuilder()
        val connection = URL(url).openConnection() as HttpURLConnection
        var data: Int = connection.inputStream.read()
        while (data != -1) {
            builder.append(data.toChar())
            data = connection.inputStream.read()
        }
        return builder.toString()
    }

    fun sendPostRequest(
        userName: String,
        password: String,
        nav: NavHostController,
        context: Context
    ): Boolean {

        val jsonObject = JSONObject()

        jsonObject.put("username", userName)
        jsonObject.put("password", password)

        val jsonObjectString = jsonObject.toString()

        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("https://comp4097-2021ass1.herokuapp.com/user/login")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.setRequestProperty(
                "Content-Type",
                "application/json"
            )
            httpURLConnection.setRequestProperty(
                "Accept",
                "application/json"
            )
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = true

            val outputStreamWriter = OutputStreamWriter(httpURLConnection.outputStream)
            outputStreamWriter.write(jsonObjectString)
            outputStreamWriter.flush()

            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = httpURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }
                withContext(Dispatchers.Main) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(JsonParser.parseString(response))

                    val jsonObject = JSONTokener(prettyJson).nextValue() as JSONObject

                    val sp = context.getSharedPreferences("user", Context.MODE_PRIVATE)
                    val editor = sp.edit()
                    val username = jsonObject.getString("username")
                    val coin = jsonObject.getInt("coin")
                    editor.putString("user", username).apply()
                    editor.putInt("coins", coin).apply()
                    Log.d("cook in login" ,httpURLConnection.getHeaderField("Set-Cookie"))
                    editor.putString("cookies",httpURLConnection.getHeaderField("Set-Cookie")).apply()
                    nav.navigate("user")
                }
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }
        return false
    }
    fun RedeemCoupon (id: String,context: Context) {

        val jsonObject = JSONObject()
        val jsonObjectString = jsonObject.toString()

        GlobalScope.launch(Dispatchers.IO) {
            Log.d("id",id)
            val url = URL("https://comp4097-2021ass1.herokuapp.com/user/QPon/add/${id}")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "PUT"
            httpURLConnection.setRequestProperty("Content-Type", "application/json")
            httpURLConnection.setRequestProperty("Accept", "application/json")
            val sp = context.getSharedPreferences("user", Context.MODE_PRIVATE)
            var cok :String = sp.getString("cookies","a")!!
            httpURLConnection.setRequestProperty("Cookie", cok)

            Log.d("cookredeem" , cok)
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = true

            val outputStreamWriter = OutputStreamWriter(httpURLConnection.outputStream)
            outputStreamWriter.write(jsonObjectString)
            outputStreamWriter.flush()

            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = httpURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }
                     withContext(Dispatchers.Main) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val receiveData = gson.toJson(JsonParser.parseString(response))

                         print("Here")
                    result = "Redeem successfully"
                }
            }

            Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())

            if (responseCode == 200) {
                result = "Redeem successfully"
            } else if (responseCode == 408) {
                result= "Not enough coins"
            } else if (responseCode == 409) {
                result = "Already redeemed"
            } else if (responseCode == 410) {
                result = "No quota"
            } else if(responseCode ==403){
                result = "Cookie Time out"
            } else {
                result = ""
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }
    }

    fun Logout (context: Context,) {

        val jsonObject = JSONObject()
        val jsonObjectString = jsonObject.toString()
        val sp = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = sp.edit()
        GlobalScope.launch(Dispatchers.IO) {

            val url = URL("https://comp4097-2021ass1.herokuapp.com/user/logout")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.setRequestProperty("Content-Type", "application/json") // The format of the content we're sending to the server
            httpURLConnection.setRequestProperty("Accept", "application/json") // The format of response we want to get from the server
            var cok :String = sp.getString("cookies","a")!!
            httpURLConnection.setRequestProperty("Cookie", cok)
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = true

            val outputStreamWriter = OutputStreamWriter(httpURLConnection.outputStream)
            outputStreamWriter.write(jsonObjectString)
            outputStreamWriter.flush()

            val responseCode = httpURLConnection.responseCode
            if (responseCode == 204) {
                editor.clear().apply()
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }

    }
}