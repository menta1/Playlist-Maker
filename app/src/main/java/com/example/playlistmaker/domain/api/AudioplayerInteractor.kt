package com.example.playlistmaker.domain.api

interface AudioplayerInteractor {
    fun play()
    fun pause()
    fun stop()
    fun preparePlayer(url: String, listner: AudioplayerState)
    fun currentPosition(): Int
    fun release()

}