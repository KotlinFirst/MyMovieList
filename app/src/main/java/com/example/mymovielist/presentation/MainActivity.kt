package com.example.mymovielist.presentation

import android.icu.lang.UCharacter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovielist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ItemMovieFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var movieListAdapter: MovieListAdapter
    private var movieItemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        movieItemContainer = findViewById(R.id.movie_item_container)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.movieList.observe(this) {
            movieListAdapter.submitList(it)
        }
        val buttonAddItem = findViewById<FloatingActionButton>(R.id.button_add_movie_item)
        buttonAddItem.setOnClickListener {
            if (verticalOrientationMode()) {
                val intent = ItemMovieActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragmentLandOrientation(ItemMovieFragment.newInstanceAddItem())
            }
        }

    }

    override fun onEditingFinished() {
        supportFragmentManager.popBackStack()
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
    }

    private fun verticalOrientationMode(): Boolean {
        return movieItemContainer == null
    }

    private fun launchFragmentLandOrientation(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.movie_item_container, fragment)
            .addToBackStack(null)
            .commit()
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
            if (verticalOrientationMode()) {
                val intent = ItemMovieActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragmentLandOrientation(ItemMovieFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setupLongClickListener() {
        movieListAdapter.movieItemOnLongClickListener = {
            viewModel.changeState(it)
        }
    }

}