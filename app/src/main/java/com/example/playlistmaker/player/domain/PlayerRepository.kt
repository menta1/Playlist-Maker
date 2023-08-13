package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.model.Track

interface PlayerRepository {
    suspend fun getClickedTrack(trackId: Int): Track?
    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(track: Track)
}