package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.AudioplayerInteractor
import com.example.playlistmaker.domain.api.AudioplayerRepository
import com.example.playlistmaker.domain.api.AudioplayerState

class AudioplayerInteractorImpl(private val repository: AudioplayerRepository) :
    AudioplayerInteractor{
    override fun play() {
        repository.play()
    }

    override fun pause() {
        repository.pause()
    }

    override fun stop() {
        repository.stop()
    }

    override fun preparePlayer(url: String, listner: AudioplayerState) {
        repository.preparePlayer(url, listner)
    }

    override fun currentPosition(): Int {
        return repository.currentPosition()
    }

    override fun release() {
        repository.release()
    }

}