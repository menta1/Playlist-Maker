package com.example.playlistmaker.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi

class InternetCheckReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context?, intent: Intent?) {
        if ((intent?.action == android.net.ConnectivityManager.CONNECTIVITY_ACTION || intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) && isConnected(
                context
            )
        ) {
            Toast.makeText(context, "Отсутствует подключение к интернету", Toast.LENGTH_SHORT)
                .show()
        }
    }
}