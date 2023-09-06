package com.example.playlistmaker.mediateka.favorite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.favorite.domain.FavoriteInteractor
import com.example.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.launch

class FavoritesTracksViewModel(private val favoriteInteractor: FavoriteInteractor) : ViewModel() {

    private val _favoriteTrackLiveData = MutableLiveData<List<Track>>()
    val favoriteTrackLiveData: LiveData<List<Track>> = _favoriteTrackLiveData

    private val _viewStateController = MutableLiveData<FavoritesTracksUiState>()
    val viewStateControllerLiveData: LiveData<FavoritesTracksUiState> = _viewStateController

    init {
        getAllTracksFavorite()
    }

    fun getAllTracksFavorite() {
        viewModelScope.launch {
            favoriteInteractor.getAllTracks().collect {
                if (it.isEmpty()) {
                    _viewStateController.postValue(FavoritesTracksUiState.EmptyFavoriteTracks)
                } else {
                    _viewStateController.postValue(FavoritesTracksUiState.HasFavoriteTracks)
                    _favoriteTrackLiveData.postValue(it)
                }
            }
        }
    }

    fun onClick(track: Track) {
        favoriteInteractor.startPlayerActivity(track)
    }
}