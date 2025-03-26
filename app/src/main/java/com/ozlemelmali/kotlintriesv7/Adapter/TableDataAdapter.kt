package com.ozlemelmali.kotlintriesv7.Adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ozlemelmali.kotlintriesv7.R

class TableDataAdapter(private var columns: List<String>, private var data: List<Map<String, Any?>>) :
    RecyclerView.Adapter<TableDataAdapter.DataViewHolder>() {

    inner class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rowLayout: LinearLayout = view.findViewById(R.id.rowLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_table_row, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.rowLayout.removeAllViews() // remove before column

        for (column in columns) {
            val textView = TextView(holder.itemView.context).apply {
                text = data[position][column]?.toString() ?: "N/A"
                setPadding(16, 8, 16, 8)
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }
            holder.rowLayout.addView(textView) // add new column
        }
    }

    override fun getItemCount(): Int = data.size

    fun updateData(newColumns: List<String>, newData: List<Map<String, Any?>>) {
        columns = newColumns
        data = newData
        notifyDataSetChanged()
    }
}



