package com.example.playlistmaker.search.data

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.util.ResultCallback

class SearchRepositoryImpl(
    private val searchHistory: SearchHistory,
    private val networkClient: NetworkClient,
    private val context: Context
) : SearchRepository {

    override fun showTrackHistory(): List<Track> {
        return searchHistory.showTrackHistory()
    }

    override fun addTrackHistory(track: Track) {
        searchHistory.addTrackHistory(track = track)
    }

    override fun clearHistory(){
        searchHistory.clearHistory()
    }

    override fun startPlayerActivity() {
        context.startActivity(
            Intent(
                context,
                PlayerActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun getTracks(trackId: Int): Track? {
        return searchHistory.getTrack(trackId = trackId)
    }

    override fun searchTracks(nameTrack: String, callback: ResultCallback) {
        networkClient.search(nameTrack, callback)
    }
}