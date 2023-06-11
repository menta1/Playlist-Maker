package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.player.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val KEY_SEARCH_HISTORY = "search_history"

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    private var tracksHistory = ArrayList<Track>()
    private val sType = object : TypeToken<ArrayList<Track>>() {}.type

    fun addTrackHistory(track: Track) {
        tracksHistory.removeAll { it.trackId == track.trackId }
        if (tracksHistory.size < 10) {
            tracksHistory.add(track)
            sharedPreferences.edit().putString(KEY_SEARCH_HISTORY, Gson().toJson(tracksHistory))
                .apply()
        } else {
            tracksHistory.removeFirst()
            tracksHistory.add(track)
            sharedPreferences.edit().putString(KEY_SEARCH_HISTORY, Gson().toJson(tracksHistory))
                .apply()
        }
    }

    fun getTrack(trackId: Int): Track? {
        val tracks = showTrackHistory()
        val track: Track? = null
        for (track in tracks) {
            if (track.trackId == trackId) {
                return track
            }
        }
        return track
    }

    fun showTrackHistory(): ArrayList<Track> {
        val r = sharedPreferences.getString(KEY_SEARCH_HISTORY, "")
        if (sharedPreferences.contains(KEY_SEARCH_HISTORY)) {
            tracksHistory = Gson().fromJson(r, sType)
        }
        tracksHistory.reverse()
        return tracksHistory
    }

    fun clearHistory(): ArrayList<Track> {
        tracksHistory.clear()
        sharedPreferences.edit().remove(KEY_SEARCH_HISTORY).apply()
        return tracksHistory
    }
}




