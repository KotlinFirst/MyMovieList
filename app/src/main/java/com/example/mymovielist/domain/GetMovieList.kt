package com.example.mymovielist.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GetMovieList(private val movieListRepository: MovieListRepository) {
    fun getMovieList(): LiveData<List<MovieItem>> {
        return movieListRepository.getMoviesList()
    }
}