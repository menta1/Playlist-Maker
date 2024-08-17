package com.example.playlistmaker.search.ui

import com.example.playlistmaker.player.domain.model.Track

sealed class SearchState {
    data object Default : SearchState()
    data object Loading : SearchState()
    data object HistoryEmpty : SearchState()
    data class HistoryNotEmpty(val tracks: List<Track>) : SearchState()
    data class SearchSuccess(val tracks: List<Track>) : SearchState()
    data object SearchFail : SearchState()
    data object SearchEmpty : SearchState()

    companion object {
        val Initial: SearchState = Default
    }
}