package com.example.playlistmaker.player.ui.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractorImpl

class PlayerViewModelFactory(context: Context) : ViewModelProvider.Factory {


    private val getPlayerRepository by lazy(LazyThreadSafetyMode.NONE) {
        PlayerRepositoryImpl(context = context)
    }
    private val getPlayerInteractor by lazy(LazyThreadSafetyMode.NONE) {
        PlayerInteractorImpl(getPlayerRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(
            getPlayerInteractor
        ) as T
    }
}