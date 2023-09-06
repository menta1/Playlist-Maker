package com.example.playlistmaker.createPlaylist.domain

import android.net.Uri

interface CreatePlaylistInteractor {
    fun saveImage(uri: Uri)
    suspend fun savePlaylist(
        title: String,
        desc: String,
        uriPick: Uri?,
        isPlaceholder: Boolean
    ): Boolean
}