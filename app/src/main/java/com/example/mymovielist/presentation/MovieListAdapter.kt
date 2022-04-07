package com.example.mymovielist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovielist.R
import com.example.mymovielist.domain.MovieItem
import java.lang.RuntimeException

class MovieListAdapter : RecyclerView.Adapter<MovieListAdapter.MovieItemViewHolder>() {
    companion object {
        const val VIEW_TYPE_TRUE = 1
        const val VIEW_TYPE_FALSE = 0
        const val MAX_POOL_VIEW_HOLDER = 15
    }
    var movieItemOnLongClickListener:((MovieItem) -> Unit)? = null

    var movieList = listOf<MovieItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
        val movieItem = movieList[position]
        holder.tvName.text = movieItem.name
        holder.tvTime.text = movieItem.time.toString()
        holder.view.setOnLongClickListener {
            movieItemOnLongClickListener?.invoke(movieItem)
            true
        }
    }

    //использую разный layout в onCreateViewHolder с помощью viewType
    override fun getItemViewType(position: Int): Int {
        return if (movieList[position].flag) {
            VIEW_TYPE_TRUE
        } else VIEW_TYPE_FALSE
    }

    //устанавливает значение по умолчанию при переиспользовании view
    override fun onViewRecycled(holder: MovieItemViewHolder) {
        super.onViewRecycled(holder)
        holder.tvName.text = ""
        holder.tvTime.text = ""
        holder.tvName.setTextColor(
            ContextCompat.getColor(
                holder.view.context,
                android.R.color.white
            )
        )
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