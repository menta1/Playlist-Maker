package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.AppThemeInteractor
import com.example.playlistmaker.settings.ui.SettingsEvent
import com.example.playlistmaker.sharing.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val appThemeInteractor: AppThemeInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(false)
    val state: StateFlow<Boolean> = _state

    init {
        _state.update {
            appThemeInteractor.initTheme()
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ShareAppButton -> {
                sharingInteractor.shareApp(event.link)
            }

            is SettingsEvent.SwitchTheme -> {
                _state.update {
                    !_state.value
                }
                appThemeInteractor.switchTheme(_state.value)

            }

            is SettingsEvent.TermsUseButton -> {
                sharingInteractor.openTerms(event.link)
            }

            is SettingsEvent.WriteSupport -> {
                sharingInteractor.openSupport(
                    EmailData(
                        email = event.email,
                        text = event.text,
                        subject = event.subject
                    )
                )
            }
        }
    }
}