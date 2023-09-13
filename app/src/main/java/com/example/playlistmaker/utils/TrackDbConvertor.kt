package com.example.playlistmaker.utils

import com.example.playlistmaker.player.db.TrackEntity
import com.example.playlistmaker.player.domain.model.Track

class TrackDbConvertor {
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.id.toString(),
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackName,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite
        )
    }


    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId.toInt(),
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackName,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite
        )
    }

    fun convertToTrack(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> this.map(track) }
    }
}