package com.example.project2.dialogs

import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.R
import com.example.project2.THSRStationRes
import com.example.project2.adapters.StationListAdapter
import com.example.project2.adapters.StationSearchAdapter
import com.example.project2.databinding.StationListBinding


class DialogStationlist(private val context: Context,
                        private var data: MutableList<THSRStationRes>,
                        private val listAdapter: StationListAdapter?,
                        private val searchAdapter: StationSearchAdapter?): AlertDialog(context) {
    private val backup = ArrayList<THSRStationRes>()
    override fun dismiss() {
        Log.e("123", "Dialog 進入 Dismiss")

        if (backup.size != data.size) {
            data.clear()
            data.addAll(backup)
        }


        super.dismiss()
    }
    override fun onStart() {
        super.onStart()
        val binding = StationListBinding.inflate(layoutInflater)
        this.setContentView(binding.root)

        if(listAdapter != null)
            binding.recyclerList.adapter = listAdapter
        if(searchAdapter != null)
            binding.recyclerList.adapter = searchAdapter

//        showSoftKeyboard(binding.edSearch)

//        val imm = binding.edSearch.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
//        //imm.showSoftInput(binding.edSearch, InputMethodManager.SHOW_IMPLICIT)
//
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT)

        window?.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

        setOnDismissListener {
            window?.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

            val res = binding.root.context.resources
            var virtualKeyboardHeight = 0
            val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) virtualKeyboardHeight = res.getDimensionPixelSize(resourceId)

            val rect = Rect()
            window?.decorView?.getWindowVisibleDisplayFrame(rect)
            val screenHeight = window?.decorView?.rootView?.height ?: 0
            val heiDifference = screenHeight - (rect.bottom + virtualKeyboardHeight)

            if (heiDifference > 0) {
                val inputMgr = binding.root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0)
            }
        }


        backup.clear()
        backup.addAll(data)

        binding.edSearch.addTextChangedListener {
            val key = binding.edSearch.text.toString()
            val r = Regex(key)
            Log.e("123", "key: $key")

            if (key.isEmpty()) {
                Log.e("123","key為空")
                data.clear()
                data.addAll(backup)
            } else {
                Log.e("123","key不為空")
                data.clear()
                backup.forEach {
                    if (r.containsMatchIn(it.StationName.Zh_tw) || r.containsMatchIn(it.StationAddress)){
                        data.add(it)
                    }
                }
            }

            if (listAdapter != null){
                listAdapter.notifyDataSetChanged()
                listAdapter?.notifyItemRangeRemoved(0, data.size)
            } else {
                searchAdapter?.notifyDataSetChanged()
                searchAdapter?.notifyItemRangeChanged(0, data.size)
            }
            Log.e("123", "data.size: ${data.size}")
        }

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerList)
        val linearLayout = LinearLayoutManager(context)
        linearLayout.orientation = LinearLayoutManager.VERTICAL
        recyclerview.layoutManager = linearLayout
        recyclerview.addItemDecoration(DividerItemDecoration(context, linearLayout.orientation))
    }

}