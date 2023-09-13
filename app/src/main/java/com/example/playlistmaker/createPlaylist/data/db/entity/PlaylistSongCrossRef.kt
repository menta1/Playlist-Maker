package com.example.playlistmaker.createPlaylist.data.db.entity

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "trackId"], tableName = "song_cross_ref_table")
data class PlaylistSongCrossRef(
    val playlistId: Int,
    val trackId: Int
)
