package com.example.playlistmaker.settings.domain

interface AppThemeRepository {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun initTheme(): Boolean
}