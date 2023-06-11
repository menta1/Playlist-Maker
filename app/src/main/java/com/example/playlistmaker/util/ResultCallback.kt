package com.example.playlistmaker.util

import com.example.playlistmaker.player.domain.model.Track

interface ResultCallback {
    fun onResultSuccess(result: Resource.Success<List<Track>>)
    fun onResultError(result: Resource.Error<Boolean>)
}