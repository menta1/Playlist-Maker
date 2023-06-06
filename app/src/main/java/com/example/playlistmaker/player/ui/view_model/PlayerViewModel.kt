package com.example.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.player.ui.activity.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(playerInteractor: PlayerInteractor) : ViewModel() {
    companion object {
        private const val TIME_RESET = "00:00"
    }

    private val mediaPlayer = MediaPlayer()
    private val _textTrackNameVariable = MutableLiveData<Track>()
    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    init {
        _playerState.value = PlayerState.STATE_DEFAULT
        _textTrackNameVariable.value = playerInteractor.getTrack()
        preparePlayer(_textTrackNameVariable.value?.previewUrl.toString())
        mediaPlayer.setOnPreparedListener {
            _playerState.value = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            _playerState.value = PlayerState.STATE_COMPLETION
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private val _textTrackTime = MutableLiveData<String>()
    val textTrackTime: LiveData<String> = _textTrackTime

    val textTrackNameVariable: LiveData<Track> = _textTrackNameVariable

    fun startPlayer() {
        _playerState.value = PlayerState.STATE_PLAYING
        mediaPlayer.start()
        handler.post(timerSong())
    }

    fun pausePlayer() {
        _playerState.value = PlayerState.STATE_PAUSED
        mediaPlayer.pause()
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    fun mediaPlayerRelease() {
        mediaPlayer.release()
    }

    private fun timerSong(): Runnable {
        return object : Runnable {
            override fun run() {
                _textTrackTime.value = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, 300)
                if (playerState.value == PlayerState.STATE_PAUSED) {
                    handler.removeCallbacks(this)
                }
                if (playerState.value == PlayerState.STATE_PREPARED) {
                    _textTrackTime.value = TIME_RESET
                }
            }
        }
    }
}