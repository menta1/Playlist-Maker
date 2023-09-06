package com.example.playlistmaker.mediateka.playlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {
    private val _playlist: MutableStateFlow<PlaylistUiState> = MutableStateFlow(
        PlaylistUiState.HasPlaylists(
            emptyList()
        )
    )
    val playlist: StateFlow<PlaylistUiState> = _playlist

    fun getPlaylists() {
        viewModelScope.launch {
            interactor.getPlaylist().collect {
                if (it.isEmpty()) {
                    _playlist.value = PlaylistUiState.EmptyPlaylist(it)
                } else {
                    _playlist.value = PlaylistUiState.HasPlaylists(it)
                }
            }
        }
    }
}