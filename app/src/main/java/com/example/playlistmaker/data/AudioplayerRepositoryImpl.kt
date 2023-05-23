package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.AudioplayerRepository
import com.example.playlistmaker.domain.api.AudioplayerState

class AudioplayerRepositoryImpl : AudioplayerRepository{
    private var mediaPlayer = MediaPlayer()

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun stop() {
    }

    override fun preparePlayer(url: String, listner: AudioplayerState) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            listner.onPreparedListener()
        }
        mediaPlayer.setOnCompletionListener {
            listner.onCompletionListener()
        }
    }

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun release() {
        mediaPlayer.release()
    }



}