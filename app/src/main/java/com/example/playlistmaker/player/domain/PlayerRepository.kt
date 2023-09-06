package com.example.playlistmaker.player.domain

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    suspend fun getClickedTrack(trackId: Int): Track?
    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    suspend fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(trackId: Int, id: Int): Boolean
}