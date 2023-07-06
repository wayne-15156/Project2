package com.example.project2

import android.app.Dialog
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CreateDialog(context: Context, val data: Array<THSRStationRes>): Dialog(context) {

    fun StationListPage() {
        val dialogStationlist = Dialog(context)
        dialogStationlist.setContentView(R.layout.station_list)
        dialogStationlist.findViewById<RecyclerView>(R.id.recyclerList).adapter =
                                                    StationListAdapter(context, data)
        //也可在RecyclerView屬性中設定
        val recyclerview = dialogStationlist.findViewById<RecyclerView>(R.id.recyclerList)
        val linearLayout = LinearLayoutManager(context)
        linearLayout.orientation = LinearLayoutManager.VERTICAL
        recyclerview.layoutManager = linearLayout
        //-----------------------------
        dialogStationlist.show()
    }


}