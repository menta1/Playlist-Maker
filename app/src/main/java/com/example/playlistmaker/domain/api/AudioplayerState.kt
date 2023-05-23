package com.example.playlistmaker.domain.api

interface AudioplayerState {
    fun onCompletionListener()
    fun onPreparedListener()
}