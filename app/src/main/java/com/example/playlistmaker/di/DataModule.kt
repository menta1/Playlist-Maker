package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.search.data.local.SearchHistoryImpl
import com.example.playlistmaker.search.data.network.ITunesSearchApi
import com.example.playlistmaker.search.data.network.NetworkClientImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ITunesSearchApi> {
        Retrofit.Builder()
            .baseUrl("http://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchApi::class.java)
    }

    single {
        androidContext().getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
    }
    factory { Gson() }

    single<SearchHistory> {
        SearchHistoryImpl(get(), get())
    }
    single<NetworkClient> {
        NetworkClientImpl(get(), get())
    }

}