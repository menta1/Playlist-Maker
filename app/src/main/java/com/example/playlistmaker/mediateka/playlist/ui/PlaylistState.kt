package com.example.playlistmaker.mediateka.playlist.ui

import com.example.playlistmaker.createPlaylist.domain.model.Playlist

sealed class PlaylistState {

    object Default : PlaylistState()
    object Empty : PlaylistState()

    class Success(val playlist: List<Playlist> ) : PlaylistState()

    companion object {
        val Initial: PlaylistState = Default
    }
}