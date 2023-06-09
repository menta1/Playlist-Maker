package com.example.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.ui.SearchModelState
import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.util.ResultCallback

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel(),
    ResultCallback {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val _viewStateController = MutableLiveData<SearchModelState>()
    val viewStateControllerLiveData: LiveData<SearchModelState> = _viewStateController

    private val _trackHistoryLiveData = MutableLiveData<ArrayList<Track>>()
    val trackHistoryLiveData: LiveData<ArrayList<Track>> = _trackHistoryLiveData

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable =
        Runnable { searchTracks() }

    var textSearch: String = ""

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
            searchDebounce()
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun searchTracks() {
        if (textSearch.isNotEmpty()) {
            searchInteractor.searchTracks(textSearch, this)
        }
    }

    fun clearHistory() {
        _trackHistoryLiveData.value = searchInteractor.clearHistory()
        _viewStateController.value = SearchModelState.HistoryEmpty
    }

    fun onClick(track: Track) {
        if (clickDebounce()) {
            searchInteractor.addTrackHistory(track)
            searchInteractor.getTracks(track.trackId)
            searchInteractor.startPlayerActivity()
        }
    }

    override fun onResultSuccess(result: Resource.Success<List<Track>>) {
        _trackHistoryLiveData.value = result.data as ArrayList<Track>?
        _viewStateController.value = SearchModelState.SearchSuccess
    }

    override fun onResultError(result: Resource.Error<Boolean>) {
        if (result.message == true) {
            _viewStateController.value = SearchModelState.SearchFail
        } else {
            _viewStateController.value = SearchModelState.SearchEmpty
        }
    }
}