package com.example.playlistmaker.mediateka.playlist.ui

import com.example.playlistmaker.createPlaylist.domain.model.Playlist

sealed class PlaylistUiState {
    class HasPlaylists(val playlist: List<Playlist>) : PlaylistUiState()
    class EmptyPlaylist(val playlist: List<Playlist>) : PlaylistUiState()
}