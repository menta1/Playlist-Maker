package com.example.playlistmaker.domain.api

interface AudioplayerRepository {
    fun play()
    fun pause()
    fun stop()
    fun preparePlayer(url: String, listner: AudioplayerState)
    fun currentPosition(): Int
    fun release()

}