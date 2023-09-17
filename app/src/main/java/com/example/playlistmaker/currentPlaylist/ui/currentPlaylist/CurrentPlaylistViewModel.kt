package com.example.playlistmaker.currentPlaylist.ui.currentPlaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.currentPlaylist.domain.CurrentPlaylistInteractor
import com.example.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentPlaylistViewModel(private val interactor: CurrentPlaylistInteractor) : ViewModel() {

    private var playlistId: Int? = null

    private val _tracks: MutableStateFlow<List<Track>> = MutableStateFlow(emptyList())
    val tracks: StateFlow<List<Track>> = _tracks

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    private val _time = MutableLiveData<Int>().apply { value = 0 }
    val time: LiveData<Int> = _time

    fun share(): Boolean {
        return interactor.sharePlaylist()
    }

    suspend fun deletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deletePlaylist()
        }
    }

    fun getTracks(id: Int) {
        playlistId = id
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getAllTracks(id).collect {
                _tracks.value = it
            }
        }
    }

    fun updatePlaylist() {
        if (playlistId != null) {
            getPlaylist(playlistId!!)
        }
    }

    fun getPlaylist(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newPlaylist = interactor.getPlaylist(id)
            withContext(Dispatchers.Main) {
                _playlist.value = newPlaylist
            }

        }
    }

    fun onClick(track: Track) {
        interactor.startPlayerActivity(track)
    }

    fun sumTimeAllTracks() {
        viewModelScope.launch(Dispatchers.Main) {
            _time.value = interactor.getSumTimeAllTracks()
        }
    }

    suspend fun deleteTrack(track: Track, playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deleteTrack(track, playlistId)
            getTracks(playlistId)
            getPlaylist(playlistId)
            sumTimeAllTracks()
        }
    }
}
