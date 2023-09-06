package com.example.playlistmaker.player.data

import com.example.playlistmaker.AppDatabase
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistSongCrossRef
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.utils.PlaylistDbConvertor
import com.example.playlistmaker.utils.TrackDbConvertor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayerRepositoryImpl(
    private val trackDbConvertor: TrackDbConvertor,
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val gson: Gson
) : PlayerRepository {
    private var openTrack: Track? = null
    override suspend fun getClickedTrack(trackId: Int): Track? {
        openTrack = appDatabase.trackDao().getTrackById(trackId.toString())
            ?.let { trackDbConvertor.map(it) }
        return openTrack
    }

    override suspend fun insertTrack(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConvertor.map(track))
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getAllPlaylists()
        emit(playlistDbConvertor.convertToPlaylist(playlists))
    }

    override suspend fun addTrackToPlaylist(trackId: Int, id: Int): Boolean {
        val playlist = appDatabase.playlistDao().getPlaylistById(id)
        appDatabase.playlistDao()
            .upsertPlaylistSongCrossRef(PlaylistSongCrossRef(playlist.playlistId!!, trackId))
        var listId: MutableList<String> = listOf<String>().toMutableList()
        if (playlist.listIdTracks.isEmpty()) {
            listId.add(trackId.toString())
            playlist.listIdTracks = gson.toJson(listId)
            playlist.countTracks++
            appDatabase.playlistDao().insertPlaylist(playlist)
            return true
        } else {
            listId =
                gson.fromJson(playlist.listIdTracks, object : TypeToken<List<String>>() {}.type)
            return if (listId.contains(trackId.toString())) {
                false
            } else {
                listId.add(trackId.toString())
                playlist.listIdTracks = gson.toJson(listId)
                playlist.countTracks++
                appDatabase.playlistDao().insertPlaylist(playlist)
                true
            }
        }
    }
}

