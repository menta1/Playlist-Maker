package com.example.playlistmaker.currentPlaylist.domain

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface CurrentPlaylistRepository {
    suspend fun getPlaylist(playlistId: Int): Playlist
    suspend fun deletePlaylist()
    fun getAllTracksById(playlistId: Int): Flow<List<Track>>
    suspend fun deleteTrack(track: Track, playlistId: Int)
    fun getSumTimeAllTracks(): Int
    fun startPlayerActivity(track: Track)
    fun sharePlaylist(): Boolean
}