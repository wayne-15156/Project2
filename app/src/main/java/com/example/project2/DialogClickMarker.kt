package com.example.project2

import android.app.Dialog
import android.content.Context
import android.widget.Adapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AlertDialogLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.databinding.StationListBinding


class DialogClickMarker(private val context: Context,
                        val select: THSRStationRes): Dialog(context) {

    override fun onStart() {
        super.onStart()
        setContentView(R.layout.marker_list)
        findViewById<TextView>(R.id.tv_title).text = "${select.StationName.Zh_tw}高鐵站"
        this.window?.setBackgroundDrawableResource(android.R.color.transparent)

    }

}