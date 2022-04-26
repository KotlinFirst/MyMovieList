package com.example.mymovielist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mymovielist.R
import com.example.mymovielist.domain.MovieItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ItemMovieActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilTime: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var etTime: TextInputEditText
    private lateinit var buttonSave: Button

    private var screenMode = MODE_UNKNOWN
    private var movieId = MovieItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_item)
        parseIntent()
        viewModel = ViewModelProvider(this)[MovieItemViewModel::class.java]
        initViews()
        addTextChangedListeners()
        launchRightMode()
        outputMassageError()
    }

    private fun outputMassageError() {
        viewModel.errorInputName.observe(this) {
            val massage = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tilName.error = massage
        }

        viewModel.errorInputTime.observe(this) {
            val massage = if (it) {
                getString(R.string.error_input_time)
            } else {
                null
            }
            tilTime.error = massage
        }
        viewModel.shouldCloseScreen.observe(this){
            finish()
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun addTextChangedListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }
        })
        etTime.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputTime()
            }
        })
    }

    private fun launchEditMode() {
        viewModel.getMovieItem(movieId)
        viewModel.movieItem.observe(this) {
            etName.setText(it.name)
            etTime.setText(it.time.toString())
        }
        buttonSave.setOnClickListener {
            viewModel.editMovieItem(etName.text?.toString(), etTime.text?.toString())
        }
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addMovieItem(etName.text?.toString(), etTime.text?.toString())
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_MOVIE_ITEM_ID)) {
                throw RuntimeException("Param movieItemId is absent")
            }
            movieId = intent.getIntExtra(EXTRA_MOVIE_ITEM_ID, MovieItem.UNDEFINED_ID)
        }
    }

    private fun initViews() {
        tilName = findViewById(R.id.til_name)
        tilTime = findViewById(R.id.til_time)
        etName = findViewById(R.id.et_name)
        etTime = findViewById(R.id.et_time)
        buttonSave = findViewById(R.id.button_save)
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_MOVIE_ITEM_ID = "extra_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ItemMovieActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, movieItemId: Int): Intent {
            val intent = Intent(context, ItemMovieActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_MOVIE_ITEM_ID, movieItemId)
            return intent
        }
    }
}