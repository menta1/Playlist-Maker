package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getTracks(trackId: Int): Track?
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
    fun showTrackHistory(): List<Track>
    suspend fun addTrackHistory(track: Track)
    fun clearHistory()
    fun startPlayerActivity(track: Track)
}