package com.example.playlistmaker.createPlaylist.domain

import android.net.Uri

interface CreatePlaylistRepository {
    fun saveImage(uri: Uri)
    suspend fun savePlaylist(
        title: String,
        desc: String,
        uriPick: Uri?,
        isPlaceholder: Boolean
    ): Boolean

    suspend fun updatePlaylistFields(
        id: Int, title: String, description: String, filePath: String,
        uriPick: Uri?
    )
}