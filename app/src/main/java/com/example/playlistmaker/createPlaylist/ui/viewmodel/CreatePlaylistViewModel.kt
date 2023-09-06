package com.example.playlistmaker.createPlaylist.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createPlaylist.domain.CreatePlaylistInteractor
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val interactor: CreatePlaylistInteractor) : ViewModel() {

    private val _successAddToDb = MutableLiveData<Boolean>()
    val successAddToDb: LiveData<Boolean> = _successAddToDb

    fun savePlaylist(title: String, description: String, uriPick: Uri?, isPlaceholder: Boolean) {
        viewModelScope.launch {
            _successAddToDb.postValue(
                interactor.savePlaylist(
                    title,
                    description,
                    uriPick,
                    isPlaceholder
                )
            )
        }
    }
}