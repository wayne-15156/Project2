package com.example.project2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class StationListAdapter(context: Context, private val stationList: Array<THSRStationRes>):
                                        RecyclerView.Adapter<StationListAdapter.ViewHolder>() {

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val tv_name = v.findViewById<TextView>(R.id.tv_name)
        val tv_addr = v.findViewById<TextView>(R.id.tv_addr)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_item, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_name.text = stationList[position].StationName.Zh_tw
        holder.tv_addr.text = stationList[position].StationAddress
    }


    override fun getItemCount(): Int {
        return stationList.size
    }
}