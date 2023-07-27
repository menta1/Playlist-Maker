package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.Response


interface NetworkClient {
    suspend fun search(dto: Any): Response
}