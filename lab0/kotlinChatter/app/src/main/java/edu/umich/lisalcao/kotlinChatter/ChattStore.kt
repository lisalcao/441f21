package edu.umich.lisalcao.kotlinChatter

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.reflect.full.declaredMemberProperties

object ChattStore {
    val chatts = arrayListOf<Chatt?>()
    private val nFields = Chatt::class.declaredMemberProperties.size

    private lateinit var queue: RequestQueue
    private val serverUrl = "https://18.220.58.92/"

    fun getChatts(context: Context, completion: () -> Unit) {
        val getRequest = JsonObjectRequest(serverUrl+"getchatts/",
            { response ->
                val chattsReceived = try { response.getJSONArray("chatts") } catch (e: JSONException) { JSONArray() }

                chatts.clear()
                for (i in 0 until chattsReceived.length()) {
                    val chattEntry = chattsReceived[i] as JSONArray
                    if (chattEntry.length() == nFields) {
                        chatts.add(Chatt(username = chattEntry[0].toString(),
                                message = chattEntry[1].toString(),
                                timestamp = chattEntry[2].toString()))
                    } else {
                        Log.e("getChatts", "Received unexpected number of fields: " + chattEntry.length().toString() + " instead of " + nFields.toString())
                    }
                }
                completion()
            }, { completion() }
        )

        if (!this::queue.isInitialized) {
            queue = newRequestQueue(context)
        }
        queue.add(getRequest)
    }

    fun postChatt(context: Context, chatt: Chatt) {
        val jsonObj = mapOf(
            "username" to chatt.username,
            "message" to chatt.message
        )
        val postRequest = JsonObjectRequest(Request.Method.POST,
            serverUrl+"postchatt/", JSONObject(jsonObj),
            { Log.d("postChatt", "chatt posted!") },
            { error -> Log.e("postChatt", error.localizedMessage ?: "JsonObjectRequest error") }
        )

        if (!this::queue.isInitialized) {
            queue = newRequestQueue(context)
        }
        queue.add(postRequest)
    }
}