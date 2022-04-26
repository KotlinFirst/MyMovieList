package com.example.mymovielist.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface MovieListRepository {

    fun addMovieItem(movieItem:MovieItem)

    fun deleteMovieItem(movieItem: MovieItem)

    fun getMovieItem(moviesItemId:Int): MovieItem

    fun editMovieItem(movieItem: MovieItem)

    fun getMoviesList(): LiveData<List<MovieItem>>
}