package com.example.playlistmaker.settings.ui.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.settings.data.AppThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.impl.AppThemeInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

class SettingsViewModelFactory(context: Context) :
    ViewModelProvider.Factory {

    private val externalNavigator by lazy(LazyThreadSafetyMode.NONE) {
        ExternalNavigatorImpl(context = context)
    }
    private val getSharingInteractor by lazy(LazyThreadSafetyMode.NONE) {
        SharingInteractorImpl(externalNavigator = externalNavigator)
    }

    private val getAppThemeRepository by lazy(LazyThreadSafetyMode.NONE) {
        AppThemeRepositoryImpl(context = context)
    }
    private val getAppThemeInteractor by lazy(LazyThreadSafetyMode.NONE) {
        AppThemeInteractorImpl(appThemeRepository = getAppThemeRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            sharingInteractor = getSharingInteractor,
            appThemeInteractor = getAppThemeInteractor
        ) as T
    }
}