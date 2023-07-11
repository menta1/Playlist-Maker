package com.example.playlistmaker.search.data

import com.example.playlistmaker.util.ResultCallback

interface NetworkClient {
    fun search(nameTrack: String, callback: ResultCallback)
}