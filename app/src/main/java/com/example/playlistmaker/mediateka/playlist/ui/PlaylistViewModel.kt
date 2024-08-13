package com.example.playlistmaker.mediateka.playlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistState.Initial)
    val state: StateFlow<PlaylistState> = _state.asStateFlow()

    init {
        getPlaylists()
    }

    fun getPlaylists() {
        viewModelScope.launch {
            interactor.getPlaylist().collect { result ->
                if (result.isEmpty()) {
                    _state.update {
                        PlaylistState.Empty
                    }
                } else {
                    _state.update {
                        PlaylistState.Success(result)
                    }
                }
            }
        }
    }
}