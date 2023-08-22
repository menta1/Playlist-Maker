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

    private val _trackIsLike = MutableLiveData<Boolean>().apply { value = false }
    val trackIsLike: LiveData<Boolean> = _trackIsLike

    private val _textTrackTime = MutableLiveData<String>()
    val textTrackTime: LiveData<String> = _textTrackTime

    override fun onCleared() {
        mediaPlayerRelease()
        super.onCleared()
    }

    fun startPlayer() {
        _viewStateController.value = PlayerModelState.Play
        mediaPlayer.start()
        timerSong()
    }

    fun pausePlayer() {
        _viewStateController.value = PlayerModelState.Pause
        if (_trackIsLike.value == false) {
            viewModelScope.launch {
                _trackLiveData.value?.let { playerInteractor.deleteTrack(it) }
            }
        }
        mediaPlayer.pause()
        timerJob?.cancel()
        _textTrackTime.value = getCurrentPlayerPosition()
    }

    fun preparePlayer(trackId: Int) {
        _viewStateController.value = PlayerModelState.Prepared
        mediaPlayer.reset()
        viewModelScope.launch {
            _trackLiveData.value = playerInteractor.getTrack(trackId)
            if (_trackLiveData.value?.isFavorite == true) {
                _trackIsLike.value = true
            }
            mediaPlayer.setDataSource(_trackLiveData.value?.previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                _viewStateController.value = PlayerModelState.Prepared
            }
            mediaPlayer.setOnCompletionListener {
                _viewStateController.value = PlayerModelState.Completion
                _viewStateController.value = PlayerModelState.Pause
                timerJob?.cancel()
                _textTrackTime.value = "00:00"
            }
        }
    }

    private fun mediaPlayerRelease() {
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
    }

    fun changeLiked() {
        if (_trackIsLike.value == true) {
            _trackIsLike.value = false
            _trackLiveData.value?.isFavorite = false
            viewModelScope.launch {
                _trackLiveData.value?.let { playerInteractor.deleteTrack(it) }
            }
        } else {
            _trackIsLike.value = true
            _trackLiveData.value?.isFavorite = true
            _trackLiveData.value?.let {
                viewModelScope.launch {
                    playerInteractor.insertTrack(it)
                }
            }
        }
    }
//
//    fun saveState(){
//
//    }
//
//    fun restoreState(){
//
//    }
}