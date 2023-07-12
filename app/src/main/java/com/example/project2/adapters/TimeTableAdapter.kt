package com.example.project2.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.DailyTimeTable
import com.example.project2.R
import com.example.project2.THSRStationRes
import com.example.project2.TrainTimeTable

class TimeTableAdapter(context: Context, private val stopStation: ArrayList<TrainTimeTable.stopTimes>,
                        private val startId: String?, private val endId: String?):
                                        RecyclerView.Adapter<TimeTableAdapter.ViewHolder>() {



    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val timeTableStation = v.findViewById<TextView>(R.id.timeTable_station)
        val timeTableDep = v.findViewById<TextView>(R.id.timeTable_dep)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.timetable_item, viewGroup, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (stopStation[position].StationID == startId ||
            stopStation[position].StationID == endId) {
            holder.timeTableStation.rootView.setBackgroundColor(0xFFFEAC00.toInt())
        }
        holder.timeTableStation.text = "${if (position + 1 < 10) "0${position+1}. " else "${position+1}. "}" +
                "${stopStation[position].StationName.Zh_tw}高鐵站"
        holder.timeTableDep.text = stopStation[position].DepartureTime

    }

    override fun getItemCount(): Int {
        return stopStation.size
    }
}