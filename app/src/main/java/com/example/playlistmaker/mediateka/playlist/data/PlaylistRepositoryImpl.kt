package com.example.playlistmaker.mediateka.playlist.data

import com.example.playlistmaker.AppDatabase
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.mediateka.playlist.domain.PlaylistRepository
import com.example.playlistmaker.utils.PlaylistDbConvertor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
) : PlaylistRepository {

    override suspend fun getPlaylist(): Flow<List<Playlist>> = flow {
        val playlist = appDatabase.playlistDao().getAllPlaylists()
        emit(playlistDbConvertor.convertToPlaylist(playlist))
    }
}