package com.example.project2

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var stationList: Array<THSRStationRes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getStation()
        setListener()
    }

    private fun setListener() {
        binding.btnSearch.setOnClickListener {
            CreateDialog(this, stationList).StationListPage()
        }

        binding.btnRoute.setOnClickListener {
        }

    }

    private fun getStation() {
        val url = "https://tdx.transportdata.tw/auth/realms/TDXConnect/protocol/openid-connect/token"
        val grantType = "client_credentials"
        val clientId = "t110368094-833838b9-4a93-4d3e"
        val clientSecret = "73609ebe-44bd-4223-bb21-9bdee5b220f9"

        val formBody = FormBody.Builder()
            .add("grant_type", grantType)
            .add("client_id", clientId)
            .add("client_secret", clientSecret)
            .build()

        val req: Request = Request.Builder()
            .url(url)
            .header("Content-Type", "application/x-www-form-urlencoded")
            //.header("User-Agent", "OkHttp Headers.java")
            //.addHeader("Accept", "application/json; q=0.5")
            //.addHeader("Accept", "application/vnd.github.v3+json")
            .post(formBody)
            .build()

        OkHttpClient().newCall(req).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onFailure", "$e")
            }
            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                val myObject = Gson().fromJson(json, tokenObject::class.java)

                val url = "https://tdx.transportdata.tw/api/basic/v2/Rail/THSR/Station"
                val req: Request = Request.Builder()
                    .addHeader("authorization", "Bearer ${myObject.access_token}")
                    .url(url)
                    .build()

                OkHttpClient().newCall(req).enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("onFailure", "$e")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val json2 = response.body?.string()
                        stationList = Gson().fromJson(json2, Array<THSRStationRes>::class.java)
                    }
                })
            }
        })
    }
}