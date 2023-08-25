package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun insertTrack(track: Track): Flow<Boolean>
    suspend fun deleteTrack(track: Track): Flow<Boolean>
    suspend fun getAllTracks(): Flow<List<Track>>
    fun startPlayerActivity(track: Track)
}