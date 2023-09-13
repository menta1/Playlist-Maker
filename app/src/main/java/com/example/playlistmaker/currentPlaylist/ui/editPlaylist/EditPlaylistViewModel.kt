package com.example.playlistmaker.currentPlaylist.ui.editPlaylist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createPlaylist.domain.CreatePlaylistInteractor
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.createPlaylist.ui.viewmodel.CreatePlaylistViewModel
import com.example.playlistmaker.currentPlaylist.domain.CurrentPlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    interactor: CreatePlaylistInteractor,
    private val interactorCurrentPlaylist: CurrentPlaylistInteractor
) :
    CreatePlaylistViewModel(interactor) {

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    fun getPlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _playlist.postValue(interactorCurrentPlaylist.getPlaylist(playlistId))
        }
    }

    fun updatePlaylistFields(title: String, description: String, uriPick: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.updatePlaylistFields(
                playlist.value!!.id!!,
                title,
                description,
                playlist.value!!.filePath,
                uriPick
            )
        }
    }
}