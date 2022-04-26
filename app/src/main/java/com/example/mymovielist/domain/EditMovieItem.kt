package com.example.mymovielist.domain

class EditMovieItem (private val movieListRepository: MovieListRepository) {
    fun editMovieItem(movieItem: MovieItem){
        movieListRepository.editMovieItem(movieItem)
    }
}