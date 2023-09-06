package com.example.playlistmaker.createPlaylist.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    val title: String,
    val description: String,
    val filePath: String,
    var listIdTracks: String,
    var countTracks: Int,
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int? = null
)
