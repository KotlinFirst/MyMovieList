package com.example.mymovielist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymovielist.data.MovieListRepositoryImpl
import com.example.mymovielist.domain.AddMovieItem
import com.example.mymovielist.domain.EditMovieItem
import com.example.mymovielist.domain.GetMovieItem
import com.example.mymovielist.domain.MovieItem
import java.lang.Exception

class MovieItemViewModel : ViewModel() {

    private val repository = MovieListRepositoryImpl

    private val addMovieItem = AddMovieItem(repository)
    private val editMovieItem = EditMovieItem(repository)
    private val getMovieItem = GetMovieItem(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean> = _errorInputName

    private val _errorInputTime = MutableLiveData<Boolean>()
    val errorInputTime: LiveData<Boolean> = _errorInputTime

    private val _movieItem = MutableLiveData<MovieItem>()
    val movieItem: LiveData<MovieItem> = _movieItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit> = _shouldCloseScreen

    fun addMovieItem(inputName: String?, inputTime: String?) {
        val name = parseInputName(inputName)
        val time = parseInputTime(inputTime)
        if (validateInput(name, time)) addMovieItem.addMovieItem(MovieItem(name, time, true))
        closeScreen()
    }

    fun editMovieItem(inputName: String?, inputTime: String?) {
        val name = parseInputName(inputName)
        val time = parseInputTime(inputTime)
        if (validateInput(name, time)) {
            _movieItem.value?.let {
                val item = it.copy(name, time)
                editMovieItem.editMovieItem(item)
                closeScreen()
            }
        }

    }

    fun getMovieItem(movieID: Int) {
        _movieItem.value = getMovieItem.getMovieItem(movieID)
    }

    private fun parseInputName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseInputTime(inputTime: String?): Int {
        return try {
            inputTime?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, time: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (time <= 0) {
            _errorInputTime.value = true
            result = false
        }
        return result
    }
    private fun closeScreen(){
        _shouldCloseScreen.value = Unit
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputTime() {
        _errorInputTime.value = false
    }
}