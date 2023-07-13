package com.example.project2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.project2.adapters.RestAdapter
import com.example.project2.databinding.ActivityRestBinding
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class RestActivity : AppCompatActivity() {

    var binding: ActivityRestBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val url = "https://api.bluenet-ride.com/v2_0/lineBot/restaurant/get"
        val lat = 25.047670364379883
        val lng = 121.51698303222656
        val range = "2000"


        val json = "{\"lastIndex\": -1," +
                    "\"count\": 15," +
                    "\"type\": [7]," +
                    "\"lat\": 25.047670364379883," +
                    "\"lng\": 121.51698303222656}," +
                    "\"range\": \"2000\"}"
        val body = json.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())

/*
//        "lastIndex": -1,
//        "count": 15,
//        "type": [7],
//        "lat": 25.047670364379883,
//        "lng": 121.51698303222656,
//        "range": "2000"
*/

        val req: Request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        OkHttpClient().newCall(req).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onFailure", "$e")
            }
            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string()
                Log.e("123", "response: $response")
                Log.e("123", "json: $res")
                val restObj = Gson().fromJson(res, RestRes::class.java)
                Log.e("123", "tokenObj: ${restObj.results.content[0].name}")

                runOnUiThread {
                    binding?.restRecycler?.adapter = RestAdapter(this@RestActivity, restObj.results.content)
                }

            }
        })
    }
}
