package com.example.playlistmaker.sharing.data


import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.ExternalNavigator
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink(appLink: String) {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, appLink)
            type = "plain/text"
            context.startActivity(this)
        }
    }

    override fun openLink(termsLink: String) {
        val termsUseButtonIntent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                termsLink
            )
        )
        context.startActivity(termsUseButtonIntent)
    }

    override fun openEmail(emailData: EmailData) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            putExtra(Intent.EXTRA_TEXT, emailData.text)
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            context.startActivity(this)
        }
    }
}