package com.example.playlistmaker.creator

import com.example.playlistmaker.data.AudioplayerRepositoryImpl
import com.example.playlistmaker.domain.impl.AudioplayerInteractorImpl

object Creator {

    fun getInteractor(): AudioplayerInteractorImpl {
        val repositoryImpl = AudioplayerRepositoryImpl()
        return AudioplayerInteractorImpl(repositoryImpl)
    }
}