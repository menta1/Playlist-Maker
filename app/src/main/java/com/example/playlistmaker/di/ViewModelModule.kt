package com.example.playlistmaker.di

import com.example.playlistmaker.createPlaylist.ui.viewmodel.CreatePlaylistViewModel
import com.example.playlistmaker.mediateka.favorite.ui.FavoritesTracksViewModel
import com.example.playlistmaker.mediateka.playlist.ui.PlaylistViewModel
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        PlayerViewModel(get())
    }
    viewModel {
        SettingsViewModel(get(), get())
    }
    viewModel {
        PlaylistViewModel(get())
    }
    viewModel {
        FavoritesTracksViewModel(get())
    }
    viewModel {
        CreatePlaylistViewModel(get())
    }
}
