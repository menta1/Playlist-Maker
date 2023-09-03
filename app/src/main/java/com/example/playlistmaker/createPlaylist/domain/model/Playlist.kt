package com.example.playlistmaker.createPlaylist.domain.model

data class Playlist(
    val title: String,
    val description: String,
    val filePath: String,
    val listIdTracks: String,
    val countTracks: Int,
    val id: Int? = null
)