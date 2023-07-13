package com.example.project2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.R
import com.example.project2.RestRes
import com.example.project2.THSRStationRes
import com.example.project2.databinding.ItemRestBinding
import com.squareup.picasso.Picasso

class RestAdapter(private val context: Context,
                  private val restList: ArrayList<RestRes.Results.Content>):
                                        RecyclerView.Adapter<RestAdapter.ViewHolder>() {

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val imgRest = v.findViewById<ImageView>(R.id.img_rest)
        val tvName = v.findViewById<TextView>(R.id.tvRest_name)
        val tvAddr = v.findViewById<TextView>(R.id.tvRest_addr)
        val tvDist = v.findViewById<TextView>(R.id.tvRest_dist)
        val tvRating = v.findViewById<TextView>(R.id.tvRest_rating)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_rest, viewGroup, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = restList[position].name
        holder.tvAddr.text = restList[position].vicinity

        holder.tvRating.text = "評價:\t${restList[position].rating}(${restList[position].reviewsNumber}則評論)"

        Picasso.get()
            .load(restList[position].photo)
            //.resize(280,221)
            .into(holder.imgRest)
    }


    override fun getItemCount(): Int {
        return restList.size
    }
}