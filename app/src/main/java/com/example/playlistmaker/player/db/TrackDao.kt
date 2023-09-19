package com.example.playlistmaker.player.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table WHERE isFavorite = 1")
    suspend fun getAllTracks(): List<TrackEntity>

    @Query("SELECT * FROM track_table WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: String): TrackEntity?

    @Query("SELECT * FROM track_table WHERE trackId = :trackId")
    suspend fun unLiked(trackId: String): TrackEntity?

    @Query("UPDATE track_table SET isFavorite = :isFavorite WHERE trackId = :trackId")
    suspend fun updateUserName(trackId: Int, isFavorite: Boolean)
}