package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.AudioplayerRepository
import com.example.playlistmaker.domain.api.AudioplayerStateListener

class AudioplayerRepositoryImpl : AudioplayerRepository {

    private val mediaPlayer = MediaPlayer()

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun stop() {
    }


    override fun preparePlayer(url: String, listener: AudioplayerStateListener) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            listener.onPreparedListener()
        }
        mediaPlayer.setOnCompletionListener {
            listener.onCompletionListener()
        }

    }

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun release() {
        mediaPlayer.release()
    }
}

