package com.example.playlistmaker.search.data

import com.example.playlistmaker.player.domain.model.Track

interface SearchHistory {
    fun addTrackHistory(track: Track)
    fun getTrack(trackId: Int): Track?
    fun showTrackHistory(): ArrayList<Track>
    fun clearHistory(): ArrayList<Track>
}