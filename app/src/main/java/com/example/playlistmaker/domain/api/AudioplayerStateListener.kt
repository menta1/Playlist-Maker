package com.example.playlistmaker.domain.api

interface AudioplayerStateListener {
    fun onCompletionListener()
    fun onPreparedListener()
}