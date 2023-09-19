package com.example.playlistmaker.di

import com.example.playlistmaker.createPlaylist.data.CreatePlaylistRepositoryImpl
import com.example.playlistmaker.createPlaylist.domain.CreatePlaylistRepository
import com.example.playlistmaker.currentPlaylist.data.CurrentPlaylistRepositoryImpl
import com.example.playlistmaker.currentPlaylist.domain.CurrentPlaylistRepository
import com.example.playlistmaker.mediateka.favorite.data.FavoriteRepositoryImpl
import com.example.playlistmaker.mediateka.favorite.domain.FavoriteRepository
import com.example.playlistmaker.mediateka.playlist.data.PlaylistRepositoryImpl
import com.example.playlistmaker.mediateka.playlist.domain.PlaylistRepository
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.settings.data.AppThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.AppThemeRepository
import com.example.playlistmaker.sharing.ExternalNavigator
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.utils.PlaylistDbConvertor
import com.example.playlistmaker.utils.TrackDbConvertor
import org.koin.dsl.module


val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get(), get(), get(), get(), get())
    }
    single<PlayerRepository> {
        PlayerRepositoryImpl(get(), get(), get(), get())
    }
    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }
    single<AppThemeRepository> {
        AppThemeRepositoryImpl(get(), get())
    }
    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get(), get())
    }
    single<CreatePlaylistRepository> {
        CreatePlaylistRepositoryImpl(get(), get(), get())
    }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }
    single<CurrentPlaylistRepository> {
        CurrentPlaylistRepositoryImpl(get(), get(), get(), get(), get())
    }
    factory { TrackDbConvertor() }
    factory { PlaylistDbConvertor() }
}
