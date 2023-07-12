package com.example.project2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.project2.adapters.DailyAdapter
import com.example.project2.databinding.ActivityDailyBinding
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.time.LocalDateTime

class DailyActivity : AppCompatActivity() {

    var binding: ActivityDailyBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        intent?.extras?.let {
            val accessToken = it.getString("token")
            val datetime = LocalDateTime.now().toString().substring(0 until 10)
            Log.e("time", datetime)
            val startId = it.getString("startId")
            val endId = it.getString("endId")

            val url = "https://tdx.transportdata.tw/api/basic/v2/Rail/THSR/DailyTimetable/OD/" +
                    "${startId}/" +
                    "to/" +
                    "${endId}/" +
                    "${datetime}?%24format=JSON"

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
                    val dailyList = Gson().fromJson(json, Array<DailyTimeTable>::class.java)
                    //Log.e("123", "${dailyList[0].DestinationStopTime.StationID}")

                    //layoutManager已在xml設好
                    runOnUiThread {
                        binding?.recyclerDaily?.adapter =
                            DailyAdapter(this@DailyActivity, dailyList, object: DailyAdapter.CodePasser {
                                override fun showText(pos: Int) {
                                    binding?.tvPath?.text = "${dailyList[pos].OriginStopTime.StationName.Zh_tw}" +
                                            "\u2192" + //→
                                            "${dailyList[pos].DestinationStopTime.StationName.Zh_tw}"
                                }

                                override fun setListener(pos: Int) {
                                    val intent = Intent(this@DailyActivity, TimeTableActivity::class.java)
                                    intent.putExtra("token", accessToken)
                                    intent.putExtra("trainId", dailyList[pos].DailyTrainInfo.TrainNo)
                                    intent.putExtra("startId", startId)
                                    intent.putExtra("endId", endId)
                                    startActivity(intent)
                                }
                            })
                    }

                }
            })
        }

    }
}