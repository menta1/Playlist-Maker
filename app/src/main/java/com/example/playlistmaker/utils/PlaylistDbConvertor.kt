package com.example.playlistmaker.utils

import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistEntity

class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.title,
            playlist.description,
            playlist.filePath,
            playlist.listIdTracks,
            playlist.countTracks
        )
    }

    private fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.title,
            playlist.description,
            playlist.filePath,
            playlist.listIdTracks,
            playlist.countTracks,
            playlist.playlistId
        )
    }

    fun convertToPlaylist(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> this.map(playlist) }
    }
}