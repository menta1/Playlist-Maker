package com.example.playlistmaker.player.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MusicService : Service(), AudioPlayerControl {

    private companion object {
        const val SERVICE_NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
    }

    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicServiceBinder()
    private var timerJob: Job? = null
    private var songUrl = ""
    private var trackName: String = ""
    private var trackArtist: String = ""

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())
    private val playerState = _playerState.asStateFlow()

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
                delay(300L)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    override fun onDestroy() {
        releasePlayer()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, "Music service", NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("$trackArtist - $trackName")
            // .setContentText(trackName)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE).build()
    }

    override fun onBind(intent: Intent?): IBinder {
        songUrl = intent?.getStringExtra("song_url") ?: ""
        trackName = intent?.getStringExtra("trackName") ?: ""
        trackArtist = intent?.getStringExtra("trackArtist") ?: ""
        initMediaPlayer()

        createNotificationChannel()
        return binder
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    private fun initMediaPlayer() {
        if (songUrl.isEmpty()) return

        mediaPlayer?.setDataSource(songUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            _playerState.value = PlayerState.Prepared()
        }
        mediaPlayer?.setOnCompletionListener {
            _playerState.value = PlayerState.Prepared()
        }
    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        return playerState
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = PlayerState.Paused(getCurrentPlayerPosition())
    }

    override fun showNotification() {
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            getForegroundServiceTypeConstant()
        )
    }

    override fun hideNotification() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun releasePlayer() {
        mediaPlayer?.stop()
        timerJob?.cancel()
        _playerState.value = PlayerState.Default()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition)
            ?: "00:00"
    }
}