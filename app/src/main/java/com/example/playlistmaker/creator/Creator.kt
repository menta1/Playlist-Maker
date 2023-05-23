package com.example.playlistmaker.creator

import com.example.playlistmaker.data.AudioplayerRepositoryImpl
import com.example.playlistmaker.domain.impl.AudioplayerInteractorImpl

object Creator {
    private val repositoryImpl = AudioplayerRepositoryImpl()
    fun getRepository(): AudioplayerInteractorImpl {
        return AudioplayerInteractorImpl(repositoryImpl)
    }
}