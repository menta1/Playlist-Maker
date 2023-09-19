package com.example.playlistmaker.currentPlaylist.domain

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

class CurrentPlaylistInteractorImpl(private val repository: CurrentPlaylistRepository) :
    CurrentPlaylistInteractor {
    override suspend fun getPlaylist(playlistId: Int): Playlist {
        return repository.getPlaylist(playlistId)
    }

    override suspend fun deletePlaylist() {
        repository.deletePlaylist()
    }

    override fun getAllTracks(playlistId: Int): Flow<List<Track>> {
        return repository.getAllTracksById(playlistId)
    }

    override suspend fun deleteTrack(track: Track, playlistId: Int): Int {
        return repository.deleteTrack(track, playlistId)
    }

    override fun getSumTimeAllTracks(): Int {
        return repository.getSumTimeAllTracks()
    }

    override fun startPlayerActivity(track: Track) {
        repository.startPlayerActivity(track)
    }

    override fun sharePlaylist(): Boolean {
        return repository.sharePlaylist()
    }
}