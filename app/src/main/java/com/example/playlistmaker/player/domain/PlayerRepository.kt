package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.model.Track

interface PlayerRepository {
    fun getTrack(): Track
}