package com.example.mymovielist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mymovielist.domain.MovieItem
import com.example.mymovielist.domain.MovieListRepository
import java.lang.RuntimeException
import kotlin.random.Random

object MovieListRepositoryImpl:MovieListRepository {
    private var movieListLD = MutableLiveData<List<MovieItem>>()
    private val movieList = sortedSetOf<MovieItem>({o1,o2->o1.id.compareTo(o2.id)})
    private var autoincrementId = 0

    init {
        for (i in 0 until 100){
            val init = MovieItem("Фильм №$i", i, Random.nextBoolean())
            addMovieItem(init)
        }
    }

    override fun addMovieItem(movieItem: MovieItem) {
        if (movieItem.id == MovieItem.UNDEFINED_ID){
            movieItem.id = autoincrementId++
        }
        movieList.add(movieItem)
        updateMovieList()
    }

    override fun deleteMovieItem(movieItem: MovieItem) {
        movieList.remove(movieItem)
        updateMovieList()
    }

    override fun getMovieItem(moviesItemId: Int): MovieItem {
        return movieList.find {
            it.id == moviesItemId }
            ?: throw RuntimeException("Element with ID $moviesItemId not found")
    }

    override fun editMovieItem(movieItem: MovieItem) {
        var oldMovieItem = getMovieItem(movieItem.id)
        movieList.remove(oldMovieItem)
        addMovieItem(movieItem)

    }

    override fun getMoviesList(): LiveData<List<MovieItem>> {
        return movieListLD
    }

    private fun updateMovieList(){
        movieListLD.value = movieList.toList()
    }
}