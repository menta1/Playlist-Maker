package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun getTracks(trackId: Int): Track?
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
    fun showTrackHistory(): List<Track>
    suspend fun addTrackHistory(track: Track)
    fun clearHistory()
    fun startPlayerActivity(track: Track)
}