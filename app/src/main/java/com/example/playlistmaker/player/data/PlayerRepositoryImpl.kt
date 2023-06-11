package com.example.playlistmaker.player.data

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlayerRepositoryImpl(
    val context: Context,
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : PlayerRepository {

    companion object {
        const val KEY_SEARCH_HISTORY = "search_history"
    }

    private val sType = object : TypeToken<ArrayList<Track>>() {}.type
    private var tracksHistory = ArrayList<Track>()

    override fun getTrack(): Track {
        val r = sharedPrefs.getString(KEY_SEARCH_HISTORY, "")
        if (sharedPrefs.contains(KEY_SEARCH_HISTORY)) {
            tracksHistory = gson.fromJson(r, sType)
        }
        tracksHistory.reverse()
        return tracksHistory[0]
    }
}

