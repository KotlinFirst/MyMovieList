package com.example.mymovielist.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovielist.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var movieListAdapter: MovieListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.movieList.observe(this) {
            movieListAdapter.submitList(it)
        }

    }

    private fun setupRecyclerView() {
        val rvMovieList = findViewById<RecyclerView>(R.id.rv_movie_list)
        with(rvMovieList) {
            movieListAdapter = MovieListAdapter()
            adapter = movieListAdapter
            recycledViewPool.setMaxRecycledViews(
                MovieListAdapter.VIEW_TYPE_TRUE,
                MovieListAdapter.MAX_POOL_VIEW_HOLDER
            )
            recycledViewPool.setMaxRecycledViews(
                MovieListAdapter.VIEW_TYPE_FALSE,
                MovieListAdapter.MAX_POOL_VIEW_HOLDER
            )
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvMovieList)
    }

    private fun setupSwipeListener(rvMovieList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = movieListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteMovieList(item)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvMovieList)
    }

    private fun setupClickListener() {
        movieListAdapter.movieItemOnClickListener = {
            Log.d("MainActivity", it.toString())
        }
    }

    private fun setupLongClickListener() {
        movieListAdapter.movieItemOnLongClickListener = {
            viewModel.changeState(it)
        }
    }

}