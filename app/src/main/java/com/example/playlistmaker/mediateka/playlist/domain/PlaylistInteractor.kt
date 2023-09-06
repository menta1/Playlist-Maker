package com.example.playlistmaker.mediateka.playlist.domain

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getPlaylist(): Flow<List<Playlist>>
}