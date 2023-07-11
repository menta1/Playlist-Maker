package com.example.playlistmaker.search.data.local

import android.content.SharedPreferences
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.util.Constants.KEY_SEARCH_HISTORY
import com.example.playlistmaker.util.Constants.MAX_SIZE_RECYCLER
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistoryImpl(private val sharedPreferences: SharedPreferences, private val gson: Gson):
    SearchHistory {
    private var tracksHistory = ArrayList<Track>()
    private val sType = object : TypeToken<ArrayList<Track>>() {}.type

    override fun addTrackHistory(track: Track) {
        tracksHistory.removeAll { it.trackId == track.trackId }
        if (tracksHistory.size < MAX_SIZE_RECYCLER) {
            tracksHistory.add(track)
            sharedPreferences.edit().putString(KEY_SEARCH_HISTORY, gson.toJson(tracksHistory))
                .apply()
        } else {
            tracksHistory.removeFirst()
            tracksHistory.add(track)
            sharedPreferences.edit().putString(KEY_SEARCH_HISTORY, gson.toJson(tracksHistory))
                .apply()
        }
    }
    override fun getTrack(trackId: Int): Track? {
        val tracks = showTrackHistory()
        val track: Track? = null
        for (track in tracks) {
            if (track.trackId == trackId) {
                return track
            }
        }
        return track
    }
    override fun showTrackHistory(): ArrayList<Track> {
        val r = sharedPreferences.getString(KEY_SEARCH_HISTORY, "")
        if (sharedPreferences.contains(KEY_SEARCH_HISTORY)) {
            tracksHistory = gson.fromJson(r, sType)
        }
        tracksHistory.reverse()
        return tracksHistory
    }
    override fun clearHistory() {
        tracksHistory.clear()
        sharedPreferences.edit().remove(KEY_SEARCH_HISTORY).apply()
    }
}




