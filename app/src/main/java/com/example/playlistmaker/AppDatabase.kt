package com.example.playlistmaker

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.createPlaylist.data.db.dao.PlaylistDao
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistSongCrossRef
import com.example.playlistmaker.player.db.TrackDao
import com.example.playlistmaker.player.db.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistSongCrossRef::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}