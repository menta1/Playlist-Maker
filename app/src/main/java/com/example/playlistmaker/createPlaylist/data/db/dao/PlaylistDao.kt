package com.example.playlistmaker.createPlaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.playlistmaker.createPlaylist.data.db.PlaylistWithSongs
import com.example.playlistmaker.createPlaylist.data.db.SongWithPlaylists
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistSongCrossRef

@Dao
interface PlaylistDao {

    @Upsert
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("UPDATE playlist_table SET title = :title, description = :description, filePath = :filePath  WHERE playlistId = :id")
    suspend fun updatePlaylistFields(id: Int, title: String, description: String, filePath: String)

    @Upsert
    suspend fun upsertPlaylistSongCrossRef(playlistSongCrossRef: PlaylistSongCrossRef): Long

    @Delete
    suspend fun deleteSongWithPlaylists(playlistSongCrossRef: PlaylistSongCrossRef): Int

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM song_cross_ref_table WHERE playlistId = :playlistId")
    suspend fun deleteByPlaylistId(playlistId: Int)

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE playlistId = :id")
    suspend fun getPlaylistById(id: Int): PlaylistEntity

    @Transaction
    @Query("SELECT * FROM playlist_table")
    fun getPlaylistsWithSongs(): List<PlaylistWithSongs>

    @Transaction
    @Query("SELECT * FROM track_table")
    fun getTracksWithPlaylists(): List<SongWithPlaylists>

    @Query("SELECT COUNT(*) FROM song_cross_ref_table WHERE trackId =:trackId")
    fun getPlaylistCountByTrackId(trackId: Int): Int
}