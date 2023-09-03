package com.example.playlistmaker.utils

import com.example.playlistmaker.db.entity.TrackEntity
import com.example.playlistmaker.db.entity.TrackInPlaylistEntity
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

    fun mapInPlaylist(track: Track): TrackInPlaylistEntity {
        return TrackInPlaylistEntity(
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
            track.id.toInt(),
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
}