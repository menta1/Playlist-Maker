package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.util.ResultCallback

interface SearchRepository {
    fun getTracks(trackId: Int): Track?
    fun searchTracks(nameTrack: String, callback: ResultCallback)
    fun showTrackHistory(): ArrayList<Track>
    fun addTrackHistory(track: Track)
    fun clearHistory(): ArrayList<Track>
    fun startPlayerActivity()
}