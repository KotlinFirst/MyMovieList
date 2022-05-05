package com.example.mymovielist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.example.mymovielist.R
import com.example.mymovielist.domain.MovieItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ItemMovieFragment : Fragment() {

    private lateinit var viewModel: MovieItemViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private lateinit var tilName: TextInputLayout
    private lateinit var tilTime: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var etTime: TextInputEditText
    private lateinit var buttonSave: Button


    private var screenMode: String = MODE_UNKNOWN
    private var movieId: Int = MovieItem.UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener){
            onEditingFinishedListener = context
        }
        else{
            throw RuntimeException("Activity must implement interface OnEditingFinishedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseParams()
        viewModel = ViewModelProvider(this)[MovieItemViewModel::class.java]
        initViews(view)
        addTextChangedListeners()
        launchRightMode()
        outputMassageError()
    }

    private fun outputMassageError() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val massage = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tilName.error = massage
        }

        viewModel.errorInputTime.observe(viewLifecycleOwner) {
            val massage = if (it) {
                getString(R.string.error_input_time)
            } else {
                null
            }
            tilTime.error = massage
        }
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
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
        viewModel.movieItem.observe(viewLifecycleOwner) {
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

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(MOVIE_ITEM_ID)) {
                throw RuntimeException("Param movieItemId is absent")
            }
            movieId = args.getInt(MOVIE_ITEM_ID, MovieItem.UNDEFINED_ID)
        }
    }


    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilTime = view.findViewById(R.id.til_time)
        etName = view.findViewById(R.id.et_name)
        etTime = view.findViewById(R.id.et_time)
        buttonSave = view.findViewById(R.id.button_save)
    }

    interface OnEditingFinishedListener {
        fun onEditingFinished(){
        }
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val MOVIE_ITEM_ID = "extra_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ItemMovieFragment {
            return ItemMovieFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }

        }

        fun newInstanceEditItem(movieId: Int): ItemMovieFragment {
            return ItemMovieFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(MOVIE_ITEM_ID, movieId)
                }
            }
        }
    }
}