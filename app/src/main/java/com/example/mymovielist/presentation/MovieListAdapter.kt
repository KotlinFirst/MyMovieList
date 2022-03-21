package com.example.mymovielist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovielist.R
import com.example.mymovielist.domain.MovieItem

class MovieListAdapter : RecyclerView.Adapter<MovieListAdapter.MovieItemViewHolder>() {

    var movieList = listOf<MovieItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    // Как создавать View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_movies_disabled,
            parent,
            false
        )
        return MovieItemViewHolder(view)
    }

    // Как вставить значения внутри этого View
    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        val movieItem = movieList[position]
        holder.tvName.text = movieItem.name
        holder.tvTime.text = movieItem.time.toString()
        holder.view.setOnLongClickListener {
            true
        }
    }

    // сколько нужно отображать
    override fun getItemCount(): Int {
        return movieList.size
    }

    class MovieItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tvNameMovie)
        val tvTime = view.findViewById<TextView>(R.id.tvTime)
    }
}