package com.example.playlistmaker.mediateka.favorite.ui

sealed class FavoritesTracksUiState {
    object HasFavoriteTracks : FavoritesTracksUiState()
    object EmptyFavoriteTracks : FavoritesTracksUiState()

}