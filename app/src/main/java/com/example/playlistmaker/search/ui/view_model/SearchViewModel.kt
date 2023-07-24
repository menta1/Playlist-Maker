package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.ui.SearchModelState
import com.example.playlistmaker.utils.Constants.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker.utils.Constants.SEARCH_DEBOUNCE_DELAY
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val _viewStateController = MutableLiveData<SearchModelState>()
    val viewStateControllerLiveData: LiveData<SearchModelState> = _viewStateController

    private val _trackHistoryLiveData = MutableLiveData<List<Track>?>()
    val trackHistoryLiveData: LiveData<List<Track>?> = _trackHistoryLiveData

    private var isClickAllowed = true

    private val searchDebounce = debounce<Boolean>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
        searchTracks()
    }

    private var textSearch: String = ""

    fun updateView() {
        if (_viewStateController.value == SearchModelState.HistoryEmpty ||
            _viewStateController.value == SearchModelState.HistoryNotEmpty ||
            _viewStateController.value == null
        ) {
            _trackHistoryLiveData.value = searchInteractor.showTrackHistory()
            if (_trackHistoryLiveData.value!!.isEmpty()) {
                _viewStateController.value = SearchModelState.HistoryEmpty
            } else {
                _viewStateController.value = SearchModelState.HistoryNotEmpty
            }
        }
    }

    fun onFocusInput() {
        _trackHistoryLiveData.value = searchInteractor.showTrackHistory()
        if (_trackHistoryLiveData.value!!.isEmpty()) {
            _viewStateController.value = SearchModelState.HistoryEmpty
        } else {
            _viewStateController.value = SearchModelState.HistoryNotEmpty
        }

    }

    fun onTextChangedInput(inputChar: CharSequence?) {
        textSearch = inputChar.toString()
        if (_viewStateController.value != SearchModelState.Loading) {
            _viewStateController.value = SearchModelState.Loading
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

    fun clearHistory() {
        searchInteractor.clearHistory()
        _viewStateController.value = SearchModelState.HistoryEmpty
    }

    fun onClick(track: Track) {
        if (clickDebounce()) {
            searchInteractor.addTrackHistory(track)
            searchInteractor.getTracks(track.trackId)
            searchInteractor.startPlayerActivity()
        }
    }

    fun searchTracks() {
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
                _viewStateController.value = SearchModelState.SearchFail
            }

            result.isEmpty() -> {
                _viewStateController.value = SearchModelState.SearchEmpty
            }

            else -> {
                _trackHistoryLiveData.value = foundTracks
                _viewStateController.value = SearchModelState.SearchSuccess
            }
        }
    }
}