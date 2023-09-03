package com.example.playlistmaker.db.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.example.playlistmaker.db.entity.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistDao {
    @Upsert
    suspend fun insertPlaylist(trackInPlaylistEntity: TrackInPlaylistEntity): Long
}