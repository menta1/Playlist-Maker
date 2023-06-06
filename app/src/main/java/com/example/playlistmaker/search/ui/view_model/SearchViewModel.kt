package com.example.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.ui.activity.TrackAdapter
import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.util.ResultCallback

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel(),
    TrackAdapter.Listener, ResultCallback {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    val adapter = TrackAdapter(this)
    private lateinit var trackHistory: ArrayList<Track>
    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable =
        Runnable { searchTracks() }

    private val _showHistoryStateViewLiveData = MutableLiveData<Boolean>()
    val showHistoryStateViewLiveData: LiveData<Boolean> = _showHistoryStateViewLiveData

    private val _answerSearch = MutableLiveData<Boolean>()
    val answerSearch: LiveData<Boolean> = _answerSearch

    private val _answerSearchSuccess = MutableLiveData<Boolean>()
    val answerSearchSuccess: LiveData<Boolean> = _answerSearchSuccess

    val inputEditText = MutableLiveData<String>()

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun showHistoryIsNotEmpty(): Boolean? {
        trackHistory = searchInteractor.showTrackHistory()
        _showHistoryStateViewLiveData.value = trackHistory.isNotEmpty()
        return _showHistoryStateViewLiveData.value
    }

    fun setTracks() {
        adapter.setTracks(trackHistory)
    }

    fun searchTracks() {
        if (inputEditText.value.toString().isNotEmpty()) {
            searchInteractor.searchTracks(inputEditText.value.toString(), this)
        }
    }

    fun clearHistory() {
        adapter.setTracks(searchInteractor.clearHistory())
    }

    override fun onClick(track: Track) {
        if (clickDebounce()) {
            searchInteractor.addTrackHistory(track)
            searchInteractor.getTracks(track.trackId)
            searchInteractor.startPlayerActivity()
        }
    }

    override fun onResultSuccess(result: Resource.Success<List<Track>>) {
        adapter.setTracks((result.data))
        _answerSearchSuccess.value = true
    }

    override fun onResultError(result: Resource.Error<Boolean>) {
        _answerSearch.value = result.message
    }
}