package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.model.Track

class PlayerInteractorImpl(private val repository: PlayerRepository) :
    PlayerInteractor {

    override fun getTrack(): Track {
        return repository.getClickedTrack()
    }
}