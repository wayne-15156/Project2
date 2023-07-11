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

class DailyAdapter(context: Context,
                   private val dailyList: Array<DailyTimeTable>,
                   private val listener: ClickOnListener):
                                        RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    interface ClickOnListener {
        fun onClickItem(pos: Int)
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val dailyTrainNo = v.findViewById<TextView>(R.id.daily_trainNo)
        val dailyDepaTime = v.findViewById<TextView>(R.id.daily_depaTime)
        val dailyTimeCost = v.findViewById<TextView>(R.id.daily_timeCost)
        val dailyArriTime = v.findViewById<TextView>(R.id.daily_arriTime)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.daily_item, viewGroup, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dailyTrainNo.text = "${if(dailyList[position].DailyTrainInfo.Direction == 0) "南下" else "北上"}\n" +
                "${dailyList[position].DailyTrainInfo.TrainNo}"
        val depTime = dailyList[position].OriginStopTime.DepartureTime
        val ariTime = dailyList[position].DestinationStopTime.ArrivalTime
        holder.dailyDepaTime.text = depTime
        holder.dailyArriTime.text = ariTime
        holder.dailyTimeCost.text = calTime(depTime, ariTime)

        listener.onClickItem(position)

        /*
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "您點擊的是: ${holder.tv_name.text}", Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun calTime(sT: String, eT: String): String {
        val sTArr = sT.split(":").map{ it.toInt()}.toTypedArray()
        val eTArr = eT.split(":").map{ it.toInt()}.toTypedArray()

        if(sTArr[1] > eTArr[1]) {
            eTArr[0]--
            eTArr[1] += 60
        }
        val hrs = eTArr[0] - sTArr[0]
        val mins = eTArr[1] - sTArr[1]

        return "${if (hrs > 0) "${hrs}時" else "" }${mins}分"
    }

    override fun getItemCount(): Int {
        return dailyList.size
    }
}