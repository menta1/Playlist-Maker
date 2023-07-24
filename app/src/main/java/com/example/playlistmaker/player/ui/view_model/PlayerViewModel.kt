package com.example.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.player.ui.PlayerModelState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {

    private val mediaPlayer = MediaPlayer()
    private var timerJob: Job? = null

    private val _viewStateController = MutableLiveData<PlayerModelState>()
    val viewStateControllerLiveData: LiveData<PlayerModelState> = _viewStateController

    private val _trackLiveData = MutableLiveData<Track>()
    val trackLiveData: LiveData<Track> = _trackLiveData

    init {
        preparePlayer()
    }

    private val _textTrackTime = MutableLiveData<String>()
    val textTrackTime: LiveData<String> = _textTrackTime

    fun startPlayer() {
        _viewStateController.value = PlayerModelState.Play
        mediaPlayer.start()
        timerSong()
    }

    fun pausePlayer() {
        _viewStateController.value = PlayerModelState.Pause
        mediaPlayer.pause()
        timerJob?.cancel()
        _textTrackTime.value = getCurrentPlayerPosition()
    }

    private fun preparePlayer() {
        _viewStateController.value = PlayerModelState.Prepared
        mediaPlayer.reset()
        _trackLiveData.value = playerInteractor.getTrack()
        mediaPlayer.setDataSource(_trackLiveData.value?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            _viewStateController.value = PlayerModelState.Prepared
        }
        mediaPlayer.setOnCompletionListener {
            _viewStateController.value = PlayerModelState.Completion
        }
    }

    fun mediaPlayerRelease() {
        mediaPlayer.release()
    }

    private fun timerSong() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(300L)
                _textTrackTime.postValue(getCurrentPlayerPosition())
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            ?: "00:00"
    }
}