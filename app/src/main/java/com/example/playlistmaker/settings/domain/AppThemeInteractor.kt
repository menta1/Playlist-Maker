package com.example.playlistmaker.settings.domain

interface AppThemeInteractor {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun initTheme(): Boolean
}