package com.example.playlistmaker.settings.ui

sealed class SettingsEvent {

    class ShareAppButton(val link: String) : SettingsEvent()

    class WriteSupport(
        val  email: String,
        val  text: String,
        val subject: String,
    ) : SettingsEvent()

    class TermsUseButton(val link: String) : SettingsEvent()

    object SwitchTheme : SettingsEvent()
}