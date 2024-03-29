package com.example.playlistmaker.currentPlaylist.data

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.AppDatabase
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistSongCrossRef
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.currentPlaylist.domain.CurrentPlaylistRepository
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.utils.PlaylistDbConvertor
import com.example.playlistmaker.utils.TrackDbConvertor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class CurrentPlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val context: Context,
    private val trackDbConvertor: TrackDbConvertor,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val gson: Gson
) : CurrentPlaylistRepository {

    private var tracks: List<Track> = emptyList()
    var playlist: Playlist? = null

    override suspend fun getPlaylist(playlistId: Int): Playlist {
        playlist = playlistDbConvertor.map(appDatabase.playlistDao().getPlaylistById(playlistId))
        getAllTracksById(playlistId)
        return playlist as Playlist
    }

    override suspend fun deletePlaylist() {
        appDatabase.playlistDao().deletePlaylist(playlistDbConvertor.map(playlist!!))
        appDatabase.playlistDao().deleteByPlaylistId(playlist!!.id!!)
    }

    override fun getAllTracksById(playlistId: Int): Flow<List<Track>> = flow {
        val playlistsWithSongsList = appDatabase.playlistDao().getPlaylistsWithSongs()
        for (playlist in playlistsWithSongsList) {
            if (playlist.playlist.playlistId == playlistId) {
                tracks = trackDbConvertor.convertToTrack(playlist.tracks)
            }
        }
        emit(tracks.reversed())
    }

    override suspend fun deleteTrack(track: Track, playlistId: Int): Int {
        val result = appDatabase.playlistDao()
            .deleteSongWithPlaylists(PlaylistSongCrossRef(playlistId, track.id))
        val playlistsCount =
            appDatabase.playlistDao().getPlaylistCountByTrackId(trackId = track.id)
        val updatedTrack = appDatabase.trackDao().getTrackById(trackId = track.id.toString())
        if (!updatedTrack!!.isFavorite && playlistsCount == 0) {
            appDatabase.trackDao().deleteTrack(trackDbConvertor.map(track))
        }
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        val listId: MutableList<String> =
            gson.fromJson(playlist.listIdTracks, object : TypeToken<List<String>>() {}.type)
        listId.remove(track.id.toString())
        playlist.listIdTracks = gson.toJson(listId)
        playlist.countTracks--
        appDatabase.playlistDao().insertPlaylist(playlist)
        return result
    }

    override fun startPlayerActivity(track: Track) {
        context.startActivity(
            Intent(
                context, PlayerActivity::class.java
            ).apply {
                putExtra("trackId", track.id)
            }.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun sharePlaylist(): Boolean {
        if (tracks.isEmpty()) {
            return false
        } else {
            val listTracksInfo: MutableList<String> = mutableListOf()
            var count = 1
            for (track in tracks) {
                listTracksInfo.add(
                    "${count++}. ${track.artistName} - ${track.trackName}(${
                        SimpleDateFormat(
                            "mm",
                            Locale.getDefault()
                        ).format(track.trackTimeMillis)
                    })"
                )
            }
            val textPlaylist =
                playlist!!.title + " " + playlist!!.countTracks + " " + changeEnding(playlist!!.countTracks)
            Intent(Intent.ACTION_SEND).apply {
                putExtra(
                    Intent.EXTRA_TEXT,
                    "$textPlaylist \n${listTracksInfo.joinToString("\n")}"
                )
                type = "plain/text"
                val chooserIntent = Intent.createChooser(this, "Выберите приложение")
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chooserIntent)
            }
            return true
        }
    }

    private fun changeEnding(count: Int): String {
        val lastDigit = count % 10
        val lastTwoDigits = count % 100

        return when {
            lastTwoDigits in 11..19 -> "треков"
            lastDigit == 1 -> "трек"
            lastDigit in 2..4 -> "трека"
            else -> "треков"
        }
    }

    override fun getSumTimeAllTracks(): Int {
        var trackList: List<Track> = emptyList()
        val playlistsWithSongsList = appDatabase.playlistDao().getPlaylistsWithSongs()
        for (playlistItem in playlistsWithSongsList) {
            if (playlistItem.playlist.playlistId == playlist?.id) {
                trackList = trackDbConvertor.convertToTrack(playlistItem.tracks)
            }
        }
        var time = 0
        for (track in trackList) {
            time += track.trackTimeMillis!!
        }
        return time
    }
}