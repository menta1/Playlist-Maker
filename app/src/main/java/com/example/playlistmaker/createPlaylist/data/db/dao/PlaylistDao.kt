package com.example.playlistmaker.createPlaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.playlistmaker.createPlaylist.data.db.PlaylistWithSongs
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistSongCrossRef

@Dao
interface PlaylistDao {

    @Upsert
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Upsert
    suspend fun upsertPlaylistSongCrossRef(playlistSongCrossRef: PlaylistSongCrossRef): Long

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE playlistId = :id")
    suspend fun getPlaylistById(id: Int): PlaylistEntity

    @Transaction
    @Query("SELECT * FROM playlist_table")
    fun getPlaylistsWithSongs(): List<PlaylistWithSongs>

    @Query("SELECT * FROM playlist_song_cross_ref")
    suspend fun getAllItems(): List<PlaylistSongCrossRef>

}