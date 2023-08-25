package com.example.playlistmaker.player.data

import android.content.Context
import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.utils.TrackDbConvertor

class PlayerRepositoryImpl(
    val context: Context,
    private val trackDbConvertor: TrackDbConvertor,
    private val appDatabase: AppDatabase
) : PlayerRepository {

    override suspend fun getClickedTrack(trackId: Int): Track? {
        return appDatabase.trackDao().getTrackById(trackId.toString())
            ?.let { trackDbConvertor.map(it) }
    }

    override suspend fun insertTrack(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConvertor.map(track))
    }
}

