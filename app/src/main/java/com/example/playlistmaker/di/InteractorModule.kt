package com.example.playlistmaker.di

import com.example.playlistmaker.createPlaylist.domain.CreatePlaylistInteractor
import com.example.playlistmaker.createPlaylist.domain.CreatePlaylistInteractorImpl
import com.example.playlistmaker.mediateka.favorite.domain.FavoriteInteractor
import com.example.playlistmaker.mediateka.favorite.domain.FavoriteInteractorImpl
import com.example.playlistmaker.mediateka.playlist.domain.PlaylistInteractor
import com.example.playlistmaker.mediateka.playlist.domain.PlaylistInteractorImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.settings.domain.AppThemeInteractor
import com.example.playlistmaker.settings.domain.impl.AppThemeInteractorImpl
import com.example.playlistmaker.sharing.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module


val interactorModule = module {
    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }
    single<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
    single<AppThemeInteractor> {
        AppThemeInteractorImpl(get())
    }
    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
    single<CreatePlaylistInteractor> {
        CreatePlaylistInteractorImpl(get())
    }
    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}
