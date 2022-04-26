package com.example.mymovielist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovielist.R

class MovieItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val tvName = view.findViewById<TextView>(R.id.tvNameMovie)
    val tvTime = view.findViewById<TextView>(R.id.tvTime)
}