package com.example.project2

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import com.example.project2.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var stationList: Array<THSRStationRes>
    private lateinit var start: THSRStationRes
    private lateinit var end: THSRStationRes
    private lateinit var select: THSRStationRes

    private lateinit var dialogStationList: DialogStationlist
    private lateinit var dialogClickMarker: DialogClickMarker

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && requestCode == 0) {
            for(result in grantResults)
                if(result != PackageManager.PERMISSION_GRANTED)
                    finish()
            loadMap()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMap()
    }
    override fun onMapReady(map: GoogleMap) {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("dbg", "定位未允許")
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
        } else {
            Log.e("dbg", "定位已授權")

            map.isMyLocationEnabled = true

            setListener(map)
            getStation(map)
        }
    }
    private fun pinPosition(map: GoogleMap) {
        runOnUiThread {
            stationList.forEach {
                val marker = MarkerOptions()
                marker.position(LatLng(it.StationPosition.PositionLat, it.StationPosition.PositionLon))
                marker.title(it.StationName.Zh_tw)
                //marker.draggable(true)
                map.addMarker(marker)
                //移動畫面
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(23.718, 120.992), 8f))
            }
        }
    }

    private fun getStation(map: GoogleMap) {
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

                        pinPosition(map)
                    }
                })
            }
        })
    }
    private fun setListener(map: GoogleMap) {

        map.setOnMarkerClickListener {marker ->
            marker.showInfoWindow()

            stationList.forEach {
                //抓出marker對應的地標物件
                if (it.StationName.Zh_tw == marker.title) {
                    select = it

                    Log.e("123", "${this@MainActivity::select.isInitialized}")
                    Log.e("select", "${select.StationName.Zh_tw}")
                    return@forEach
                }
            }
            dialogClickMarker = DialogClickMarker(this, select)
            dialogClickMarker.show()
            dialogClickMarker.findViewById<Button>(R.id.btn_start).setOnClickListener {
                start = select
                binding.autoStart.text = "高鐵站"
                //Log.e("123", "${this@MainActivity::select.isInitialized}")
                dialogClickMarker.dismiss()
            }
            dialogClickMarker.findViewById<Button>(R.id.btn_end).setOnClickListener {
                end = select
                binding.autoEnd.text = "${end.StationName.Zh_tw}+高鐵站"
                dialogClickMarker.dismiss()
            }


            true
        }

        binding.autoStart.addTextChangedListener {
            if (::dialogStationList.isInitialized)
                dialogStationList.dismiss()
            val stationName = binding.autoStart.text.subSequence(0, binding.autoStart.text.length-3)
            stationList.forEach {
                if (it.StationName.Zh_tw == stationName.toString()) {
                    start = it
                    Log.d("123", start.StationName.Zh_tw)
                    return@forEach
                }
            }
        }

        binding.autoEnd.addTextChangedListener {
            dialogStationList.dismiss()
//            val stationName = binding.autoEnd.text.subSequence(0, binding.autoEnd.text.length-3)
//            stationList.forEach {
//                if (it.StationName.Zh_tw == stationName.toString()) {
//                    end = it
//                    Log.d("123", end.StationName.Zh_tw)
//                    return@forEach
//                }
//            }
        }

        binding.autoStart.setOnClickListener {
            dialogStationList = DialogStationlist(this, stationList,
                    StationListAdapter(this, binding.autoStart,  stationList,
                        object: StationListAdapter.ClickOnListener{
                            override fun onClickItem(position: Int) {
                                binding.autoStart.text = stationList[position].StationName.Zh_tw
                            }
                        })
            )
            dialogStationList.show()
        }

        binding.autoEnd.setOnClickListener {
            dialogStationList = DialogStationlist(this, stationList,
                StationListAdapter(this, binding.autoEnd,  stationList,
                    object: StationListAdapter.ClickOnListener{
                        override fun onClickItem(position: Int) {
                            binding.autoEnd.text = stationList[position].StationName.Zh_tw
                            start = stationList[position]
                        }
                    }
                )
            )
            dialogStationList.show()
        }


        binding.btnSearch.setOnClickListener {
            dialogStationList = DialogStationlist(this, stationList, StationSearchAdapter(this,
                stationList, object: StationSearchAdapter.ClickOnListener{
                    //Callback
                    override fun onClickItem(position: Int) {
                        Log.e("123", "$position")
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(stationList[position].StationPosition.PositionLat,
                                       stationList[position].StationPosition.PositionLon), 15f))
                        dialogStationList.dismiss()
                    }
                }
            ))
            dialogStationList.show()
        }

        binding.btnRoute.setOnClickListener {

        }

        binding.btnRev.setOnClickListener {
            if (::start.isInitialized && ::end.isInitialized) {
                val swap = start
                start = end
                end = swap

                val swap2 = binding.autoStart.text
                binding.autoStart.text = binding.autoEnd.text
                binding.autoEnd.text = swap2
            }
        }
    }
    private fun loadMap() {
        val map = supportFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment
        map.getMapAsync(this)
    }
}