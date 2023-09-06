package com.example.playlistmaker.createPlaylist.data.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistSongCrossRef
import com.example.playlistmaker.player.db.TrackEntity

data class PlaylistWithSongs(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "trackId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val songs: List<TrackEntity>
)