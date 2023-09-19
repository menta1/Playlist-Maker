package com.example.playlistmaker.player.domain

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    suspend fun getTrack(trackId: Int): Track?
    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(trackId: Int, playlistId: Int, isFavorite: Boolean)
    suspend fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(trackId: Int, id: Int): Boolean
}