package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.model.Track

class PlayerInteractorImpl(private val repository: PlayerRepository) :
    PlayerInteractor {

    override suspend fun getTrack(trackId: Int): Track? {
        return repository.getClickedTrack(trackId)
    }

    override suspend fun insertTrack(track: Track) {
        repository.insertTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        repository.deleteTrack(track)
    }
}