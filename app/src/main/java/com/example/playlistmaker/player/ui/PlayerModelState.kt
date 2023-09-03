package com.example.playlistmaker.player.ui

sealed class PlayerModelState {
    object Play : PlayerModelState()
    object Pause : PlayerModelState()
    object Completion : PlayerModelState()
    object Prepared : PlayerModelState()
}