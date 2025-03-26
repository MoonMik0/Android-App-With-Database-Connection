package com.ozlemelmali.kotlintriesv7.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ozlemelmali.kotlintriesv7.R

class TableHeaderAdapter(private var columns: List<String>) :
    RecyclerView.Adapter<TableHeaderAdapter.HeaderViewHolder>() {

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textColumnHeader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_table_header, parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.textView.text = columns[position]
    }

    override fun getItemCount(): Int = columns.size

    fun updateColumns(newColumns: List<String>) {
        columns = newColumns
        notifyDataSetChanged()
    }
}

