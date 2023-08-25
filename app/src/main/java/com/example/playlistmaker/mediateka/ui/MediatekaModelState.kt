package com.example.playlistmaker.mediateka.ui

sealed class MediatekaModelState {
    object HasFavoriteTracks: MediatekaModelState()
    object EmptyFavoriteTracks: MediatekaModelState()
}