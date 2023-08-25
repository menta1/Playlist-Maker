package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.model.Track

interface PlayerInteractor {
    suspend fun getTrack(trackId: Int): Track?
    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(track: Track)
}