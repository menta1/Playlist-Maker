package com.example.playlistmaker.player.domain.model

data class Track(
    val id: Int,
    val artistName: String?,
    val trackTimeMillis: Int?,
    val artworkUrl100: String?,
    val artworkUrl60: String?,
    val trackName: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    var isFavorite: Boolean = false
)

