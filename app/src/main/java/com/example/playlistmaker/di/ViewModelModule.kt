package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.ui.view_model.FavoritesTracksViewModel
import com.example.playlistmaker.mediateka.ui.view_model.PlaylistViewModel
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
        PlaylistViewModel()
    }
    viewModel {
        FavoritesTracksViewModel(get())
    }
}
