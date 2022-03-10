package com.example.mymovielist.domain

data class MovieItem(
                     val name: String,
                     val duration: Int,
                     var flag: Boolean,
                     var id: Int = UNDEFINED_ID){

    companion object{
        const val UNDEFINED_ID = -1
    }
}

