package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.AppThemeInteractor
import com.example.playlistmaker.sharing.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val appThemeInteractor: AppThemeInteractor
) : ViewModel() {

    private val _isChecked = MutableLiveData<Boolean>()
    val isCheckedTrue: LiveData<Boolean> = _isChecked

    fun shareAppButton(link: String) {
        sharingInteractor.shareApp(link)
    }

    fun writeSupportButton(
        email: String,
        text: String,
        subject: String,
    ) {
        sharingInteractor.openSupport(EmailData(email = email, text = text, subject = subject))
    }

    fun termsUseButton(link: String) {
        sharingInteractor.openTerms(link)
    }

    fun initTheme() {
        _isChecked.value = appThemeInteractor.initTheme()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        appThemeInteractor.switchTheme(darkThemeEnabled)
    }
}