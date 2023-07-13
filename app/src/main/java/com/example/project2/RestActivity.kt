package com.example.project2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.project2.adapters.RestAdapter
import com.example.project2.databinding.ActivityRestBinding
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.SphericalUtil
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

        var lat: Double? = null
        var lng: Double? = null

        intent.extras?.let {
            lat = it.getDouble("stationLat")
            lng = it.getDouble("stationLng")
        }

        val url = "https://api.bluenet-ride.com/v2_0/lineBot/restaurant/get"


        val json = "{\"lastIndex\": -1," +
                    "\"count\": 100," +
                    "\"type\": [7]," +
                    "\"lat\": ${lat}," +
                    "\"lng\": ${lng}}," +
                    "\"range\": \"2000\"}"
        val body = json.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())

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
                val restObj = Gson().fromJson(res, RestRes::class.java)
                Log.e("123", "restObj: ${restObj}")
                val restList = ArrayList<RestRes.Results.Content>()
                restList.addAll(restObj.results.content)
                //保留至小數點後一位
                restList.forEach {
                    it.dist = (SphericalUtil.computeDistanceBetween(LatLng(lat!!, lng!!), LatLng(it.lat, it.lng))/100).toInt().toDouble()/10
                }
                val restListOrdered = ArrayList<RestRes.Results.Content>()
                restListOrdered.addAll(restList.sortedBy {it.dist})
                val restListFinal = ArrayList<RestRes.Results.Content>()
                restListFinal.addAll(restListOrdered.take(15))


                Log.e("123", "Rest size: ${restList.size}")
                runOnUiThread {
                    binding?.restRecycler?.adapter = RestAdapter(this@RestActivity, restListFinal, LatLng(lat!!, lng!!))
                }

            }
        })
    }
}
