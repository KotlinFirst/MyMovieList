package com.example.mymovielist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovielist.R
import com.example.mymovielist.domain.MovieItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var movieListAdapter: MovieListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.movieList.observe(this) {
            movieListAdapter.movieList = it
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
        movieListAdapter.movieItemOnLongClickListener = {
            viewModel.changeState(it)
        }
    }

}