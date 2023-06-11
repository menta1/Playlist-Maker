package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.util.ResultCallback

interface SearchInteractor {
    fun getTracks(trackId: Int): Track?
    fun searchTracks(nameTrack: String, callback: ResultCallback)
    fun showTrackHistory(): List<Track>
    fun addTrackHistory(track: Track)
    fun clearHistory()
    fun startPlayerActivity()
}