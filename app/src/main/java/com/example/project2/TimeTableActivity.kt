package com.example.project2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.project2.adapters.DailyAdapter
import com.example.project2.adapters.TimeTableAdapter
import com.example.project2.databinding.ActivityTimeTableBinding
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class TimeTableActivity : AppCompatActivity() {

    var binding: ActivityTimeTableBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeTableBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        intent?.extras?.let {
            val accessToken = it.getString("token")
            val trainId = it.getString("trainId")
            val startId = it.getString("startId")
            val endId = it.getString("endId")

            val url = "https://tdx.transportdata.tw/api/basic/v2/Rail/THSR/DailyTimetable/Today/TrainNo/"+
                    "${trainId}?%24format=JSON"


            val req: Request = Request.Builder()
                .addHeader("authorization", "Bearer $accessToken")
                .url(url)
                .build()

            OkHttpClient().newCall(req).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("onFailure", "$e")
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body?.string()
                    //Log.e("123", "$response")
                    val timeTable = Gson().fromJson(json, Array<TrainTimeTable>::class.java)
                    //Log.e("123", "${timeTable[0].DailyTrainInfo.TrainNo}")

                    //layoutManager已在xml設好
                    runOnUiThread {
                        binding?.tvTimeTableTitle?.text = "${timeTable[0].DailyTrainInfo.TrainNo}時刻表"
                        binding?.timeTableRecycler?.adapter =
                            TimeTableAdapter(this@TimeTableActivity, timeTable[0].StopTimes, startId, endId)
                    }

                }
            })
        }
    }
}