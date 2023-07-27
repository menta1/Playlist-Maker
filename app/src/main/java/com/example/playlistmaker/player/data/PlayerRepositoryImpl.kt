package com.example.playlistmaker.player.data

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.utils.Constants.KEY_SEARCH_HISTORY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlayerRepositoryImpl(
    val context: Context,
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : PlayerRepository {

    private val sType = object : TypeToken<ArrayList<Track>>() {}.type
    private var tracksHistory = ArrayList<Track>()

    override fun getClickedTrack(): Track? {
        val r = sharedPrefs.getString(KEY_SEARCH_HISTORY, "")
        if (sharedPrefs.contains(KEY_SEARCH_HISTORY)) {
            tracksHistory = gson.fromJson(r, sType)
        }
        return tracksHistory.firstOrNull()
    }
}

