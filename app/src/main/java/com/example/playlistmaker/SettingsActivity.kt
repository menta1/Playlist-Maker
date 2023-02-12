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
        val termsUseButton  = findViewById<Button>(R.id.termsUse)
        backButton.setOnClickListener{
            finish()
        }

        shareAppButton.setOnClickListener{
            val shareAppButtonIntent = Intent(Intent.ACTION_SEND)
            shareAppButtonIntent.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/")
            shareAppButtonIntent.type = "plain/text"

            startActivity(shareAppButtonIntent)
        }
        writeSupportButton.setOnClickListener{
            val writeSupportButtonIntent = Intent(Intent.ACTION_SENDTO)
            writeSupportButtonIntent.data = Uri.parse("mailto:")
            writeSupportButtonIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("exen1993@gmail.com"))
            writeSupportButtonIntent.putExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!")
            writeSupportButtonIntent.putExtra(Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
            startActivity(writeSupportButtonIntent)
        }
        termsUseButton.setOnClickListener{
            val termsUseButtonIntent = Intent(Intent.ACTION_VIEW, Uri.parse(
                "https://yandex.ru/legal/practicum_offer/"))
            startActivity(termsUseButtonIntent)
        }
    }

}