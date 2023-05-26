package com.example.playlistmaker.domain.impl

import android.content.Context
import com.example.playlistmaker.domain.api.AudioplayerInteractor
import com.example.playlistmaker.domain.api.AudioplayerRepository
import com.example.playlistmaker.domain.api.AudioplayerStateListener

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

    override fun preparePlayer(url: String, listener: AudioplayerStateListener) {
        repository.preparePlayer(url, listener)
    }

    override fun currentPosition(): Int {
        return repository.currentPosition()
    }

    override fun release() {
        repository.release()
    }

}