package com.example.playlistmaker.settings.ui

sealed class SettingsState {

    object Default : SettingsState()
    object DarkThemeEnabled : SettingsState()
    object DarkThemeOff : SettingsState()

    companion object{
        val Initial: SettingsState = Default
    }
}