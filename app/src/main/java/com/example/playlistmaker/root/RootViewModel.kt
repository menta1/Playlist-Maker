package com.example.playlistmaker.root

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.AppThemeInteractor

class RootViewModel(
   private val interactor: AppThemeInteractor
) : ViewModel() {

    fun initTheme(){
        interactor.initTheme()
    }
}