package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.util.ResultCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkClientImpl(
    private val iTunesSearchApi: ITunesSearchApi
) : NetworkClient {
    override fun search(nameTrack: String, callback: ResultCallback) {
        iTunesSearchApi.search(nameTrack)
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