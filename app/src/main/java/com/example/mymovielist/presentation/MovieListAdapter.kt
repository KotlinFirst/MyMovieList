package com.example.mymovielist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.mymovielist.R
import com.example.mymovielist.domain.MovieItem

class MovieListAdapter : ListAdapter<MovieItem, MovieItemViewHolder>(MovieItemDiffCallback()) {

    var movieItemOnLongClickListener: ((MovieItem) -> Unit)? = null
    var movieItemOnClickListener: ((MovieItem) -> Unit)? = null

    // Как создавать View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_TRUE -> R.layout.item_movies_enabled
            VIEW_TYPE_FALSE -> R.layout.item_movies_disabled
            else -> throw RuntimeException("ViewType not found: $viewType")
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        return MovieItemViewHolder(view)
    }

    // Как вставить значения внутри этого View
    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        val movieItem = getItem(position)
        holder.view.setOnLongClickListener {
            movieItemOnLongClickListener?.invoke(movieItem)
            true
        }
        holder.view.setOnClickListener {
            movieItemOnClickListener?.invoke((movieItem))
        }
        holder.tvName.text = movieItem.name
        holder.tvTime.text = movieItem.time.toString()
    }

    //использую разный layout в onCreateViewHolder с помощью viewType
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).flag) {
            VIEW_TYPE_TRUE
        } else VIEW_TYPE_FALSE
    }

    companion object {
        const val VIEW_TYPE_TRUE = 1
        const val VIEW_TYPE_FALSE = 0
        const val MAX_POOL_VIEW_HOLDER = 15
    }
}