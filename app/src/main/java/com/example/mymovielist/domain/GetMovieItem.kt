package com.example.mymovielist.domain

class GetMovieItem(private val movieListRepository: MovieListRepository) {
    fun getMovieItem(moviesItemId:Int): MovieItem {
        return movieListRepository.getMovieItem(moviesItemId)
    }
}