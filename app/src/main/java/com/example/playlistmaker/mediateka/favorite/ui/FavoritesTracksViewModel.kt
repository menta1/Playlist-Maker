package com.example.playlistmaker.mediateka.favorite.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.favorite.domain.FavoriteInteractor
import com.example.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesTracksViewModel(private val favoriteInteractor: FavoriteInteractor) : ViewModel() {

    private val _state = MutableStateFlow(FavoriteState.Initial)
    val state: StateFlow<FavoriteState> = _state.asStateFlow()

    init {
        getAllTracksFavorite()
    }

    fun getAllTracksFavorite() {
        viewModelScope.launch {
            favoriteInteractor.getAllTracks().collect { result ->
                if (result.isEmpty()) {
                    _state.update {
                        FavoriteState.Empty
                    }
                } else {
                    _state.update {
                        FavoriteState.Success(result)
                    }
                }
            }
        }
    }

    fun onClick(track: Track) {
        favoriteInteractor.startPlayerActivity(track)
    }
}