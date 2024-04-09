package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.player.ui.AudioPlayerControl
import com.example.playlistmaker.player.ui.PlayerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {

    private var playlistId = 0

    private val _trackLiveData = MutableLiveData<Track>()
    val trackLiveData: LiveData<Track> = _trackLiveData

    private val _trackIsLike = MutableLiveData<Boolean>().apply { value = false }
    val trackIsLike: LiveData<Boolean> = _trackIsLike

    private val _textTrackTime = MutableLiveData<String>()
    val textTrackTime: LiveData<String> = _textTrackTime

    private val _playlist: MutableStateFlow<List<Playlist>> = MutableStateFlow(emptyList())
    val playlist: StateFlow<List<Playlist>> = _playlist

    private val _resultAdding = MutableLiveData<Boolean>()
    val resultAdding: LiveData<Boolean> = _resultAdding

    private val _stateForCreatePlaylist = MutableLiveData<Boolean>()
    val stateForCreatePlaylist: LiveData<Boolean> = _stateForCreatePlaylist

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState

    private var audioPlayerControl: AudioPlayerControl? = null

    fun showNotification() {
        audioPlayerControl?.showNotification()
    }

    fun hideNotification() {
        audioPlayerControl?.hideNotification()
    }

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl.getPlayerState().collect { state ->
                playerState.value = state
                if (state is PlayerState.Prepared) hideNotification()
                _textTrackTime.postValue(state.progress)
            }
        }
    }

    fun onPlayerButtonClicked(isPlaying: Boolean) {
        if (isPlaying) {
            audioPlayerControl?.pausePlayer()
        } else {
            audioPlayerControl?.startPlayer()
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    init {
        getAllPlaylists()
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerControl = null
    }

    fun getAllPlaylists() {
        viewModelScope.launch {
            playerInteractor.getAllPlaylists().collect {
                _playlist.value = it
            }
        }
    }

    fun onClick(trackId: Int, id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _resultAdding.postValue(playerInteractor.addTrackToPlaylist(trackId, id))
                playerInteractor.getAllPlaylists().collect {
                    _playlist.value = it
                }
            }
        }
    }

    fun openViewCreatePlaylist(result: Boolean) {
        _stateForCreatePlaylist.postValue(result)
    }

    fun checkLike(playlistId: Int) {
        this.playlistId = playlistId
        if (_trackIsLike.value == false) {
            viewModelScope.launch(Dispatchers.IO) {
                _trackLiveData.value?.let { playerInteractor.deleteTrack(it.id, playlistId, false) }
            }
        }
    }

    fun preparePlayer(trackId: Int) {
        viewModelScope.launch {
            _trackLiveData.value = playerInteractor.getTrack(trackId)
            if (_trackLiveData.value?.isFavorite == true) {
                _trackIsLike.value = true
            }
        }
    }

    fun changeLiked() {
        if (_trackIsLike.value == true) {
            _trackIsLike.value = false
            _trackLiveData.value?.isFavorite = false
            viewModelScope.launch(Dispatchers.IO) {
                _trackLiveData.value?.let { playerInteractor.deleteTrack(it.id, playlistId, false) }
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
}