package com.example.mymovielist.domain

class DeleteMovieItem(private val movieListRepository: MovieListRepository) {
    fun deleteMovieItem(movieItem: MovieItem){
        movieListRepository.deleteMovieItem(movieItem)

    }
}