package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.backButtonSettingsActivity)
        val shareAppButton = findViewById<Button>(R.id.shareApp)
        val writeSupportButton = findViewById<Button>(R.id.writeSupport)
        val termsUseButton = findViewById<Button>(R.id.termsUse)
        backButton.setOnClickListener {
            finish()
        }

        shareAppButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_link))
                type = "plain/text"
                startActivity(this)
            }
        }
        writeSupportButton.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.extra_text_write_email)))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.extra_text_write_support))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.extra_text_write_subject))
                startActivity(this)
            }
        }
        termsUseButton.setOnClickListener {
            val termsUseButtonIntent = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    getString(R.string.terms_use_link)
                )
            )
            startActivity(termsUseButtonIntent)
        }
    }

}