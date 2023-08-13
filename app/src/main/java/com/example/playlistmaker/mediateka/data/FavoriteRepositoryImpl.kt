package com.example.playlistmaker.mediateka.data

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.db.entity.TrackEntity
import com.example.playlistmaker.mediateka.domain.FavoriteRepository
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.utils.TrackDbConvertor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val context: Context,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoriteRepository {

    override suspend fun insertTrack(track: Track): Flow<Boolean> = flow {
        appDatabase.trackDao().insertTrack(convertToTrackEntity(track))
        emit(true)
    }

    override suspend fun deleteTrack(track: Track): Flow<Boolean> = flow {
        appDatabase.trackDao().deleteTrack(convertToTrackEntity(track))
        emit(true)
    }

    override suspend fun getAllTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getAllTracks().reversed()
        emit(convertFromTrackEntity(tracks))
    }

    override fun startPlayerActivity(track: Track) {
        context.startActivity(
            Intent(
                context, PlayerActivity::class.java
            ).apply {
                putExtra("trackId", track.id)
            }.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

    private fun convertToTrackEntity(track: Track): TrackEntity {
        return trackDbConvertor.map(track)
    }
}