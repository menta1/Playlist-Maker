package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.ExternalNavigator
import com.example.playlistmaker.sharing.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun shareApp(appLink: String) {
        externalNavigator.shareLink(appLink)
    }

    override fun openTerms(termsLink: String) {
        externalNavigator.openLink(termsLink)
    }

    override fun openSupport(emailData: EmailData) {
        externalNavigator.openEmail(emailData)
    }
}