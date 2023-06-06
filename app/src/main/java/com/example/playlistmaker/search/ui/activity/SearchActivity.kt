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
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModelFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, SearchViewModelFactory(this))[SearchViewModel::class.java]

        if (viewModel.showHistoryIsNotEmpty() == false)
            binding.clearHistory.visibility = View.GONE

        binding.recyclerSongItem.layoutManager = LinearLayoutManager(this)
        binding.recyclerSongItem.adapter = viewModel.adapter

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.clearButton.setOnClickListener {
            viewModel.showHistoryIsNotEmpty()
            binding.inputEditText.setText("")
            it.hideKeyboard()
            viewModel.setTracks()
            with(binding) {
                progressBar.visibility = View.GONE
                placeholderNothingWasFound.visibility = View.GONE
                placeholderCommunicationProblems.visibility = View.GONE
                recyclerSongItem.visibility = View.VISIBLE
                recyclerLayout.visibility = View.VISIBLE
                clearHistory.visibility = View.VISIBLE
            }
            if (viewModel.showHistoryStateViewLiveData.value == false) {
                binding.clearHistory.visibility = View.GONE
                binding.youSearch.visibility = View.GONE
            }
        }

        viewModel.showHistoryStateViewLiveData.observe(this) {
            clearButtonState(it)
        }
        viewModel.answerSearch.observe(this) {
            visibleContent(it)
        }
        viewModel.answerSearchSuccess.observe(this) {
            with(binding) {
                progressBar.visibility = View.GONE
                recyclerSongItem.visibility = View.VISIBLE
                recyclerLayout.visibility = View.VISIBLE
                youSearch.visibility = View.GONE
                clearHistory.visibility = View.GONE
            }
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.visibility = clearButtonVisibility(s)
                viewModel.showHistoryIsNotEmpty()
                if (viewModel.showHistoryStateViewLiveData.value == true) {
                    viewModel.setTracks()
                    binding.youSearch.visibility = View.VISIBLE
                } else if (binding.inputEditText.hasFocus() && s?.isEmpty() == true) {
                    binding.youSearch.visibility = View.GONE
                    binding.recyclerSongItem.visibility = View.GONE
                } else binding.youSearch.visibility = View.GONE
                if (s?.isNotEmpty() == true) {
                    viewModel.searchDebounce()
                    binding.youSearch.visibility = View.GONE
                    binding.recyclerLayout.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.placeholderCommunicationProblems.visibility = View.GONE
                    binding.placeholderNothingWasFound.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.inputEditText.value = s?.toString()
            }
        }

        binding.inputEditText.addTextChangedListener(searchTextWatcher)

        binding.refreshButton.setOnClickListener {
            viewModel.searchTracks()
        }

        binding.clearHistory.setOnClickListener {
            binding.inputEditText.setText("")
            it.hideKeyboard()
            binding.clearHistory.visibility = View.GONE
            binding.youSearch.visibility = View.GONE
            viewModel.clearHistory()
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.progressBar.visibility = View.VISIBLE
                binding.placeholderCommunicationProblems.visibility = View.GONE
                binding.placeholderNothingWasFound.visibility = View.GONE
                viewModel.searchTracks()
            }
            false
        }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (viewModel.showHistoryStateViewLiveData.value == true) {
                binding.youSearch.visibility = if (hasFocus && binding.inputEditText.text.isEmpty())
                    View.VISIBLE else View.GONE
                if (binding.youSearch.visibility == View.VISIBLE) {
                    viewModel.setTracks()
                    binding.recyclerSongItem.visibility = View.VISIBLE
                    binding.clearHistory.visibility = View.VISIBLE
                } else {
                    viewModel.setTracks()
                }
            }
        }
    }
    private fun clearButtonState(it: Boolean) {
        binding.progressBar.visibility = View.GONE
        binding.placeholderNothingWasFound.visibility = View.GONE
        binding.placeholderCommunicationProblems.visibility = View.GONE
        binding.recyclerSongItem.visibility = View.VISIBLE
        binding.recyclerLayout.visibility = View.VISIBLE
        binding.clearHistory.visibility = View.VISIBLE
        if (viewModel.showHistoryStateViewLiveData.value == false)
            binding.clearHistory.visibility = View.GONE
        if (it) {
            binding.youSearch.visibility = View.GONE
            binding.clearHistory.visibility = View.GONE
        }
    }

    private fun visibleContent(fail: Boolean) {
        if (fail) {
            binding.progressBar.visibility = View.GONE
            binding.recyclerSongItem.visibility = View.GONE
            binding.recyclerLayout.visibility = View.GONE
            binding.placeholderNothingWasFound.visibility = View.GONE
            binding.placeholderCommunicationProblems.visibility = View.VISIBLE
            binding.refreshButton.visibility = View.VISIBLE
        } else {
            binding.refreshButton.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.recyclerSongItem.visibility = View.GONE
            binding.recyclerLayout.visibility = View.GONE
            binding.placeholderNothingWasFound.visibility = View.VISIBLE
        }
    }

    fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}