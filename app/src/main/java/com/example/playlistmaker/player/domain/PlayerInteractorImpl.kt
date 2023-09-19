package com.example.playlistmaker.player.domain

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(private val repository: PlayerRepository) :
    PlayerInteractor {

    override suspend fun getTrack(trackId: Int): Track? {
        return repository.getClickedTrack(trackId)
    }

    override suspend fun insertTrack(track: Track) {
        repository.insertTrack(track)
    }

    override suspend fun deleteTrack(trackId: Int, playlistId: Int, isFavorite: Boolean) {
        repository.deleteTrack(trackId, playlistId, isFavorite)
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    override suspend fun addTrackToPlaylist(trackId: Int, id: Int): Boolean {
        return repository.addTrackToPlaylist(trackId, id)
    }
}