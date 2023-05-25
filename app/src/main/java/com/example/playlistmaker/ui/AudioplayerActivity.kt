package com.example.playlistmaker.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.domain.api.AudioplayerStateListener
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity : AppCompatActivity(),AudioplayerStateListener {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIME_RESET = "00:00"
    }
    private lateinit var binding: ActivityAudioplayerBinding
    //private val mediaPlayer = MediaPlayer.cre
    private val repository = Creator.getInteractor()

    private var playerState = STATE_DEFAULT
    private var url = ""
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textTrackNameVariable.isSelected = true
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.textTrackTime.text = TIME_RESET
        val track: Track? = intent.extras?.getParcelable("TRACK")
        url = track?.previewUrl.toString()
        repository.preparePlayer(url, this)
        with(binding) {
            textTrackNameVariable.text = track?.trackName
            textArtistNameVariable.text = track?.artistName
            textTrackTimeMillisVariable.text = (SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track?.trackTimeMillis)).toString()
            textCollectionNameVariable.text = track?.let { collectionNameIsEmpty(it) }
            textReleaseDateVariable.text = track?.releaseDate?.substring(0, 4)
            textPrimaryGenreNameVariable.text = track?.primaryGenreName
            textCountryVariable.text = track?.country
        }
        Glide.with(binding.root)
            .load(track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_high)
            .centerCrop()
            .transform(RoundedCorners(binding.root.resources.getDimensionPixelSize(R.dimen.rounded_artworkUrl100_high)))
            .into(binding.placeholderArtworkUrl100)

        binding.buttonPlay.setOnClickListener {
            playbackControl()
        }
        binding.buttonPause.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerState = STATE_PAUSED
        repository.release()
    }

    private fun collectionNameIsEmpty(track: Track): String {
        return if (track.collectionName?.isEmpty() == true) {
            with(binding) {
                textCollectionName.visibility = View.GONE
                textCollectionNameVariable.visibility = View.GONE
            }
            ""
        } else {
            with(binding) {
                textCollectionName.visibility = View.VISIBLE
                textCollectionNameVariable.visibility = View.VISIBLE
            }
            track.collectionName.toString()
        }
    }
    override fun onCompletionListener() {
        binding.buttonPlay.isEnabled = true
        playerState = STATE_PREPARED
    }
    override fun onPreparedListener() {
        playerState = STATE_PREPARED
        binding.buttonPlay.visibility = View.VISIBLE
        binding.buttonPause.visibility = View.GONE
    }
   
    private fun startPlayer() {
        playerState = STATE_PLAYING
        repository.play()
        binding.buttonPlay.visibility = View.GONE
        binding.buttonPause.visibility = View.VISIBLE
        handler.post(timerSong())

    }

    private fun pausePlayer() {
        playerState = STATE_PAUSED
        repository.pause()
        binding.buttonPlay.visibility = View.VISIBLE
        binding.buttonPause.visibility = View.GONE

    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun timerSong(): Runnable {
        return object : Runnable {
            override fun run() {
                binding.textTrackTime.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(repository.currentPosition())
                handler.postDelayed(this, 300)
                if (playerState == STATE_PAUSED) {
                    handler.removeCallbacks(this)
                }
                if (playerState == STATE_PREPARED) {
                    binding.textTrackTime.text = TIME_RESET
                }
            }
        }
    }





}
