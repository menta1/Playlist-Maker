package com.example.playlistmaker.search.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.network.ITunesSearchApi
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.util.ResultCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchRepositoryImpl(val context: Context) : SearchRepository {

    companion object {
        const val iTunesBaseUrl = "http://itunes.apple.com"
        const val SHARED_PREF = "shared_pref"
    }

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
    private val searchHistory: SearchHistory = SearchHistory(sharedPrefs)

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)

    override fun showTrackHistory(): ArrayList<Track> {
        return searchHistory.showTrackHistory()
    }

    override fun addTrackHistory(track: Track) {
        searchHistory.addTrackHistory(track = track)
    }

    override fun clearHistory(): ArrayList<Track> {
        return searchHistory.clearHistory()
    }

    override fun startPlayerActivity() {
        context.startActivity(Intent(context, PlayerActivity::class.java))
    }

    override fun getTracks(trackId: Int): Track? {
        return searchHistory.getTrack(trackId = trackId)
    }

    override fun searchTracks(nameTrack: String, callback: ResultCallback) {
        iTunesService.search(nameTrack)
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            callback.onResultSuccess(Resource.Success((response.body()!!.results)))

                        } else {
                            callback.onResultError(Resource.Error(false))
                        }
                    } else {
                        callback.onResultError(Resource.Error(false))
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    callback.onResultError(Resource.Error(true))
                }
            })
    }
}