package com.example.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.player.ui.PlayerModelState
import com.example.playlistmaker.util.Constants.TIME_RESET
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {

    private val mediaPlayer = MediaPlayer()

    private val _viewStateController = MutableLiveData<PlayerModelState>()
    val viewStateControllerLiveData: LiveData<PlayerModelState> = _viewStateController

    private val _trackLiveData = MutableLiveData<Track>()
    val trackLiveData: LiveData<Track> = _trackLiveData

    init {
        mediaPlayer.setOnPreparedListener {
            _viewStateController.value = PlayerModelState.Prepared
        }
        mediaPlayer.setOnCompletionListener {
            _viewStateController.value = PlayerModelState.Completion
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private val _textTrackTime = MutableLiveData<String>()
    val textTrackTime: LiveData<String> = _textTrackTime

    fun getTrack() {
        _trackLiveData.value = playerInteractor.getTrack()
        preparePlayer(_trackLiveData.value!!.previewUrl)
    }

    fun startPlayer() {
        _viewStateController.value = PlayerModelState.Play
        mediaPlayer.start()
        handler.post(timerSong())
    }

    fun pausePlayer() {
        _viewStateController.value = PlayerModelState.Pause
        mediaPlayer.pause()
    }

    private fun preparePlayer(url: String) {
        _viewStateController.value = PlayerModelState.Prepared
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
                if (_viewStateController.value == PlayerModelState.Pause) {
                    handler.removeCallbacks(this)
                }
                if (_viewStateController.value == PlayerModelState.Prepared) {
                    _textTrackTime.value = TIME_RESET
                }
            }
        }
    }
}