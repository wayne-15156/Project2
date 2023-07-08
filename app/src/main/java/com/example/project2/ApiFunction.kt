package com.example.project2

import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class ApiFunction(map: GoogleMap, opCode: Int) {
    private lateinit var stationList: Array<THSRStationRes>


    //加至MainActivity Code
    fun te() {
        getStation {
            
        }
    }

    fun getStation(returnValue: (Array<THSRStationRes>) -> Unit) {
        val url = "https://tdx.transportdata.tw/auth/realms/TDXConnect/protocol/openid-connect/token"
        val grantType = "client_credentials"
        val clientId = "t108360243-1bc46cc7-d2ca-4707"
        val clientSecret = "fa0358f0-7de8-4d88-a8d3-24540f8effb2"

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
                        returnValue(stationList)
                    }
                })
            }
        })
    }
}