package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.ui.Clear
import com.example.playlistmaker.search.ui.Search
import com.example.playlistmaker.search.ui.SearchEvent
import com.example.playlistmaker.search.ui.SearchState
import com.example.playlistmaker.utils.Constants.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker.utils.Constants.SEARCH_DEBOUNCE_DELAY
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val _state = MutableStateFlow(SearchState.Initial)
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var isClickAllowed = true

    private val searchDebounce = debounce<Boolean>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
        searchTracks()
    }

    private var textSearch: String = ""

    fun onEvent(event: SearchEvent) {
        when (event) {
            Clear.ClearHistory -> clearHistory()
            Clear.ClearTextField -> {
                textSearch = ""
                onFocusInput()
            }
            is Search.ClickTrack -> onClick(track = event.track)
            Search.RefreshSearch -> refreshSearch()
            is Search.TextChangedInput -> onTextChangedInput(text = event.text)
        }
    }

    private fun onFocusInput() {
        viewModelScope.launch(Dispatchers.IO) {
            val history = searchInteractor.showTrackHistory()
            if (history.isEmpty()) {
                _state.update { SearchState.HistoryEmpty }
            } else {
                _state.update { SearchState.HistoryNotEmpty(history) }
            }
        }
    }

    private fun onTextChangedInput(text: String) {
        textSearch = text
        if (_state.value != SearchState.Loading) {
            _state.update { SearchState.Loading }
            searchDebounce(true)
        }
    }

    private fun refreshSearch() {
        if (_state.value != SearchState.Loading) {
            _state.update { SearchState.Loading }
            searchDebounce(true)
        }
    }

    private fun clickDebounce(): Boolean {
        val clickDebounce = debounce<Boolean>(CLICK_DEBOUNCE_DELAY, viewModelScope, true) {
            isClickAllowed = true
        }
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            clickDebounce(true)
        }
        return current
    }

    private fun clearHistory() {
        searchInteractor.clearHistory()
        _state.update { SearchState.HistoryEmpty }
    }


    private fun onClick(track: Track) {
        if (clickDebounce()) {
            viewModelScope.launch { searchInteractor.addTrackHistory(track) }
            searchInteractor.getTracks(track.id)
            searchInteractor.startPlayerActivity(track)
        }
    }

    private fun searchTracks() {
        if (textSearch.isNotEmpty()) {
            viewModelScope.launch {
                searchInteractor.searchTracks(textSearch)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val result = mutableListOf<Track>()
        if (foundTracks != null) {
            result.addAll(foundTracks)
        }
        when {
            errorMessage != null -> {
                _state.update {
                    SearchState.SearchFail
                }
            }

            result.isEmpty() -> {
                _state.update { SearchState.SearchEmpty }
            }

            else -> {
                _state.update {
                    SearchState.SearchSuccess(result)
                }
            }
        }
    }
}