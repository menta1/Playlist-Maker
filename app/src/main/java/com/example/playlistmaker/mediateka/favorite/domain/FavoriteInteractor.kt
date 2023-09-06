package com.example.playlistmaker.mediateka.favorite.domain

import com.example.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    suspend fun getAllTracks(): Flow<List<Track>>
    fun startPlayerActivity(track: Track)
}