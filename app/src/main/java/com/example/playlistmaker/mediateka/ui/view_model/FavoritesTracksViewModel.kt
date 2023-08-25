package com.example.playlistmaker.mediateka.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.FavoriteInteractor
import com.example.playlistmaker.mediateka.ui.MediatekaModelState
import com.example.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.launch

class FavoritesTracksViewModel(private val favoriteInteractor: FavoriteInteractor) : ViewModel() {

    private val _favoriteTrackLiveData = MutableLiveData<List<Track>>()
    val favoriteTrackLiveData: LiveData<List<Track>> = _favoriteTrackLiveData

    private val _viewStateController = MutableLiveData<MediatekaModelState>()
    val viewStateControllerLiveData: LiveData<MediatekaModelState> = _viewStateController

    init {
        getAllTracksFavorite()
    }

    fun getAllTracksFavorite() {
        viewModelScope.launch {
            favoriteInteractor.getAllTracks().collect {
                if (it.isEmpty()) {
                    _viewStateController.postValue(MediatekaModelState.EmptyFavoriteTracks)
                } else {
                    _viewStateController.postValue(MediatekaModelState.HasFavoriteTracks)
                    _favoriteTrackLiveData.postValue(it)
                }
            }
        }
    }

    fun onClick(track: Track) {
        favoriteInteractor.startPlayerActivity(track)
    }
}