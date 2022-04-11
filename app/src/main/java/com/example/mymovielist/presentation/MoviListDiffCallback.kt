package com.example.mymovielist.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.mymovielist.domain.MovieItem

class MoviListDiffCallback(
    private val oldList: List<MovieItem>,
    private val newList: List<MovieItem>
):DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}