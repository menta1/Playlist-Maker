package com.example.playlistmaker.createPlaylist.domain

import android.net.Uri

class CreatePlaylistInteractorImpl(private val repository: CreatePlaylistRepository) :
    CreatePlaylistInteractor {
    override fun saveImage(uri: Uri) {
        repository.saveImage(uri)
    }

    override suspend fun savePlaylist(
        title: String,
        desc: String,
        uriPick: Uri?,
        isPlaceholder: Boolean
    ): Boolean {
        return repository.savePlaylist(title, desc, uriPick, isPlaceholder)
    }

    override suspend fun updatePlaylistFields(
        id: Int,
        title: String,
        description: String,
        filePath: String,
        uriPick: Uri?
    ) {
        repository.updatePlaylistFields(id, title, description, filePath, uriPick)
    }
}