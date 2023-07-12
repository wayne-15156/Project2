package com.example.project2.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.R
import com.example.project2.THSRStationRes
import com.example.project2.adapters.StationListAdapter
import com.example.project2.adapters.StationSearchAdapter


class DialogStationlist(private val context: Context,
                        var data: Array<THSRStationRes>,
                        val adapter: Any): Dialog(context) {

    override fun onStart() {
        super.onStart()
        this.setContentView(R.layout.station_list)

        if (adapter is StationListAdapter)
            findViewById<RecyclerView>(R.id.recyclerList).adapter = adapter
        else
            findViewById<RecyclerView>(R.id.recyclerList).adapter = adapter as StationSearchAdapter

        val data2 = data.toMutableList()
        val backup = data2


        findViewById<EditText>(R.id.ed_search).addTextChangedListener {

        }

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerList)
        val linearLayout = LinearLayoutManager(context)
        linearLayout.orientation = LinearLayoutManager.VERTICAL
        recyclerview.layoutManager = linearLayout
        recyclerview.addItemDecoration(DividerItemDecoration(context, linearLayout.orientation))
    }
}