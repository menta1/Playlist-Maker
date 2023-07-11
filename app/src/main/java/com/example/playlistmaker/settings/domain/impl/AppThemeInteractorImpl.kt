package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.AppThemeInteractor
import com.example.playlistmaker.settings.domain.AppThemeRepository

class AppThemeInteractorImpl(private val appThemeRepository: AppThemeRepository) :
    AppThemeInteractor {
    override fun switchTheme(darkThemeEnabled: Boolean) {
        appThemeRepository.switchTheme(darkThemeEnabled)
    }

    override fun initTheme(): Boolean {
        return appThemeRepository.initTheme()
    }
}