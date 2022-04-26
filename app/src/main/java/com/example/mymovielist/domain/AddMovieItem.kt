package com.example.mymovielist.domain

class AddMovieItem(private val movieListRepository: MovieListRepository){
    fun addMovieItem(movieItem:MovieItem){
        movieListRepository.addMovieItem(movieItem)
    }
}