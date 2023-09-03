package com.example.playlistmaker.mediateka.playlist.domain

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun getPlaylist(): Flow<List<Playlist>> {
        return repository.getPlaylist()
    }
}