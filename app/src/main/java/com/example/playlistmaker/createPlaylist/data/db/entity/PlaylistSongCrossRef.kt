package com.example.playlistmaker.createPlaylist.data.db.entity

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "trackId"], tableName = "playlist_song_cross_ref" )
data class PlaylistSongCrossRef(
    val playlistId: Int,
    val trackId: Int
)
