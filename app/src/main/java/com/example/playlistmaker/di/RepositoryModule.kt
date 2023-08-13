package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.data.FavoriteRepositoryImpl
import com.example.playlistmaker.utils.TrackDbConvertor
import com.example.playlistmaker.mediateka.domain.FavoriteRepository
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.settings.data.AppThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.AppThemeRepository
import com.example.playlistmaker.sharing.ExternalNavigator
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import org.koin.dsl.module


val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get(), get(), get(), get(), get())
    }
    single<PlayerRepository> {
        PlayerRepositoryImpl(get(), get(), get())
    }
    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }
    single<AppThemeRepository> {
        AppThemeRepositoryImpl(get(), get())
    }
    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(),get(),get())
    }
    factory { TrackDbConvertor() }
}
