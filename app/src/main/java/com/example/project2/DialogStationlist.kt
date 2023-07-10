package com.example.project2

import android.app.Dialog
import android.content.Context
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class DialogStationlist(private val context: Context,
                        val data: Array<THSRStationRes>,
                        val adapter: Any): Dialog(context) {

    override fun onStart() {
        super.onStart()

        this.setContentView(R.layout.station_list)

        if (adapter is StationListAdapter)
            this.findViewById<RecyclerView>(R.id.recyclerList).adapter = adapter
        else
            this.findViewById<RecyclerView>(R.id.recyclerList).adapter = adapter as StationSearchAdapter

        val recyclerview = this.findViewById<RecyclerView>(R.id.recyclerList)
        val linearLayout = LinearLayoutManager(context)
        linearLayout.orientation = LinearLayoutManager.VERTICAL
        recyclerview.layoutManager = linearLayout
        recyclerview.addItemDecoration(DividerItemDecoration(context, linearLayout.orientation))
    }

    /*fun StationListPage() {
        val dialogStationlist = Dialog(context)
        dialogStationlist.setContentView(R.layout.station_list)
        dialogStationlist.findViewById<RecyclerView>(R.id.recyclerList).adapter =
             StationListAdapter(context,v,  data, object: StationListAdapter.ClickOnListener{
                        override fun onClickItem(position: Int) {
                            v.text = data[position].StationName.Zh_tw
                        }
                    })
        //也可在RecyclerView屬性中設定
        val recyclerview = dialogStationlist.findViewById<RecyclerView>(R.id.recyclerList)
        val linearLayout = LinearLayoutManager(context)
        linearLayout.orientation = LinearLayoutManager.VERTICAL
        recyclerview.layoutManager = linearLayout
        //加入分割線
        recyclerview.addItemDecoration(DividerItemDecoration(context, linearLayout.orientation))

        dialogStationlist.show()
    }*/

}