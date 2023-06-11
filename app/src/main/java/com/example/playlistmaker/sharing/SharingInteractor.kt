package com.example.playlistmaker.sharing

import com.example.playlistmaker.sharing.domain.model.EmailData

interface SharingInteractor {
    fun shareApp(appLink: String)
    fun openTerms(termsLink: String)
    fun openSupport(emailData: EmailData)
}