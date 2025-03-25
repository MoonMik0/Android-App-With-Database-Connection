package com.ozlemelmali.kotlintriesv7.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TableDataAdapter(private val data: List<Map<String, Any>>) :
    RecyclerView.Adapter<TableDataAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rowData = data[position]
        holder.textView.text = rowData.entries.joinToString(" | ") { "${it.key}: ${it.value}" }
    }

    override fun getItemCount(): Int = data.size
}
