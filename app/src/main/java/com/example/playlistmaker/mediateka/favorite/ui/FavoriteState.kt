package com.example.playlistmaker.mediateka.favorite.ui

import com.example.playlistmaker.player.domain.model.Track

sealed class FavoriteState {

    data object Default : FavoriteState()

    class Success(val tracks: List<Track>) : FavoriteState()

    data object Empty : FavoriteState()

    companion object {
        val Initial: FavoriteState = Default
    }
}