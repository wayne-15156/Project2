package com.example.project2.dialogs

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
import kotlinx.coroutines.selects.select


class DialogClickMarker(context: Context): Dialog(context) {

    override fun onStart() {
        super.onStart()
        this.window?.setBackgroundDrawableResource(android.R.color.transparent)

    }
}