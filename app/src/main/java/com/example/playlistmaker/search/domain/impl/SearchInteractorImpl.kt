package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.util.ResultCallback

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {
    override fun getTracks(trackId: Int): Track? {
        return searchRepository.getTracks(trackId)
    }

    override fun searchTracks(nameTrack: String, callback: ResultCallback) {
        return searchRepository.searchTracks(nameTrack = nameTrack, callback)
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