package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {
    override fun getTracks(trackId: Int): Track? {
        return searchRepository.getTracks(trackId)
    }

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return searchRepository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun showTrackHistory(): List<Track> {
        return searchRepository.showTrackHistory()
    }

    override fun addTrackHistory(track: Track) {
        searchRepository.addTrackHistory(track)
    }

    override fun clearHistory() {
        searchRepository.clearHistory()
    }

    override fun startPlayerActivity() {
        searchRepository.startPlayerActivity()
    }
}