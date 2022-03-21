package com.example.mymovielist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymovielist.data.MovieListRepositoryImpl
import com.example.mymovielist.domain.*

class MainViewModel:ViewModel() {

    private val repository = MovieListRepositoryImpl

    private val getMovieList = GetMovieList(repository)
    private val deleteMovieItem = DeleteMovieItem(repository)
    private val editMovieItem = EditMovieItem(repository)

    val movieList = getMovieList.getMovieList()


    fun deleteMovieList(movieItem: MovieItem){
        val list = deleteMovieItem.deleteMovieItem(movieItem)
    }
// меняет активность фильма(flag)
    fun changeState(movieItem: MovieItem){
        val newMovieItem = movieItem.copy(flag = !movieItem.flag )
        editMovieItem.editMovieItem(newMovieItem)
    }

}