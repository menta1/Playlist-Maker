package com.example.playlistmaker.search.data

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.data.dto.TrackRequest
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val searchHistory: SearchHistory,
    private val networkClient: NetworkClient,
    private val context: Context
) : SearchRepository {

    override fun showTrackHistory(): List<Track> {
        return searchHistory.showTrackHistory()
    }

    override fun addTrackHistory(track: Track) {
        searchHistory.addTrackHistory(track = track)
    }

    override fun clearHistory() {
        searchHistory.clearHistory()
    }

    override fun startPlayerActivity() {
        context.startActivity(
            Intent(
                context, PlayerActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun getTracks(trackId: Int): Track? {
        return searchHistory.getTrack(trackId = trackId)
    }

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.search(TrackRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as TrackResponse) {
                    val data = results.map {
                        Track(
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTimeMillis = it.trackTimeMillis,
                            artworkUrl100 = it.artworkUrl100,
                            trackId = it.trackId,
                            collectionName = it.collectionName,
                            releaseDate = it.releaseDate,
                            primaryGenreName = it.primaryGenreName,
                            country = it.country,
                            previewUrl = it.previewUrl
                        )
                    }
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}