package com.example.playlistmaker.domain.api

import android.content.Context

interface AudioplayerInteractor {
    fun play()
    fun pause()
    fun stop()
    fun preparePlayer(url: String, listener: AudioplayerStateListener)
    fun currentPosition(): Int
    fun release()

}