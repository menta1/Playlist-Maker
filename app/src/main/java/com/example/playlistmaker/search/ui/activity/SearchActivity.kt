package com.example.playlistmaker.search.ui.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.search.ui.SearchModelState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModelFactory

class SearchActivity : AppCompatActivity(), TrackAdapter.Listener {

    private lateinit var viewModel: SearchViewModel

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, SearchViewModelFactory(this))[SearchViewModel::class.java]

        val adapter = TrackAdapter(this)
        binding.recyclerSongItem.adapter = adapter
        binding.recyclerSongItem.layoutManager = LinearLayoutManager(this)
        viewModel.trackHistoryLiveData.observe(this) {
            adapter.setTracks(it)
        }

        viewModel.viewStateControllerLiveData.observe(this) { state ->
            when (state) {
                is SearchModelState.Loading -> stateLoading()
                is SearchModelState.HistoryEmpty -> stateHistoryEmpty()
                is SearchModelState.HistoryNotEmpty -> stateHistoryNotEmpty()
                is SearchModelState.SearchSuccess -> stateSearchSuccess()
                is SearchModelState.SearchFail -> stateSearchFail()
                is SearchModelState.SearchEmpty -> stateSearchEmpty()
            }
        }


        binding.backButton.setOnClickListener {
            finish()
        }

        binding.clearButton.setOnClickListener {
            viewModel.onFocusInput()
            binding.inputEditText.setText("")
            it.hideKeyboard()
        }


        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onTextChangedInput(s)
                if (binding.inputEditText.hasFocus() && s?.isEmpty() == true) {
                    viewModel.onFocusInput()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.refreshButton.setOnClickListener {
            viewModel.searchTracks()
        }

        binding.clearHistory.setOnClickListener {
            binding.inputEditText.setText("")
            it.hideKeyboard()
            viewModel.clearHistory()
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchTracks()
            }
            false
        }

        binding.inputEditText.setOnFocusChangeListener { _, _ ->
            viewModel.onFocusInput()
        }
    }

    private fun stateLoading() {
        binding.youSearch.visibility = View.GONE
        binding.recyclerLayout.visibility = View.GONE
        binding.recyclerSongItem.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.placeholderCommunicationProblems.visibility = View.GONE
        binding.placeholderNothingWasFound.visibility = View.GONE
    }

    private fun stateHistoryEmpty() {
        binding.youSearch.visibility = View.GONE
        binding.recyclerLayout.visibility = View.GONE
        binding.recyclerSongItem.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.clearButton.visibility = View.GONE
    }

    private fun stateHistoryNotEmpty() {
        binding.youSearch.visibility = View.VISIBLE
        binding.recyclerLayout.visibility = View.VISIBLE
        binding.recyclerSongItem.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.clearButton.visibility = View.GONE
        binding.clearHistory.visibility = View.VISIBLE
    }

    private fun stateSearchSuccess() {
        binding.youSearch.visibility = View.GONE
        binding.recyclerLayout.visibility = View.VISIBLE
        binding.recyclerSongItem.visibility = View.VISIBLE
        binding.clearHistory.visibility = View.GONE
        binding.clearButton.visibility = View.VISIBLE
    }

    private fun stateSearchFail() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerSongItem.visibility = View.GONE
        binding.recyclerLayout.visibility = View.GONE
        binding.placeholderNothingWasFound.visibility = View.GONE
        binding.placeholderCommunicationProblems.visibility = View.VISIBLE
        binding.refreshButton.visibility = View.VISIBLE
    }

    private fun stateSearchEmpty() {
        binding.refreshButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.recyclerSongItem.visibility = View.GONE
        binding.recyclerLayout.visibility = View.GONE
        binding.placeholderNothingWasFound.visibility = View.VISIBLE
        binding.placeholderCommunicationProblems.visibility = View.GONE
    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onClick(track: Track) {
        viewModel.onClick(track)
    }
}