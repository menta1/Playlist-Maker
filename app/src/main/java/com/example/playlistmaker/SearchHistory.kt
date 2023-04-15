package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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




