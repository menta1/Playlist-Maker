package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.player.ui.PlayerModelState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackId = intent.getIntExtra("trackId", -1)

        if (savedInstanceState == null) {
            viewModel.preparePlayer(trackId)
        }

        viewModel.trackIsLike.observe(this) {
            if (it) {
                binding.buttonLike.setImageResource(R.drawable.button_heard_like)
            } else binding.buttonLike.setImageResource(R.drawable.button_heard_notlike)
        }

        viewModel.trackLiveData.observe(this) {
            with(binding) {
                textTrackNameVariable.text = it.trackName
                textArtistNameVariable.text = it.artistName
                textTrackTimeMillisVariable.text = (SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(it.trackTimeMillis)).toString()
                textCollectionNameVariable.text =
                    collectionNameIsEmpty(it)
                textReleaseDateVariable.text =
                    it.releaseDate?.substring(0, 4)
                textPrimaryGenreNameVariable.text =
                    it.primaryGenreName
                textCountryVariable.text = it.country
            }
            Glide.with(binding.root)
                .load(
                    it.artworkUrl100?.replaceAfterLast(
                        '/',
                        "512x512bb.jpg"
                    )
                )
                .placeholder(R.drawable.placeholder_high)
                .centerCrop()
                .transform(RoundedCorners(binding.root.resources.getDimensionPixelSize(R.dimen.rounded_artworkUrl100_high)))
                .into(binding.placeholderArtworkUrl100)
        }

        viewModel.viewStateControllerLiveData.observe(this) { state ->
            when (state) {
                is PlayerModelState.Play -> statePlay()
                is PlayerModelState.Pause -> statePause()
                is PlayerModelState.Prepared -> statePrepared()
                is PlayerModelState.Completion -> stateCompletion()
            }
        }

        binding.textTrackNameVariable.isSelected = true
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.buttonPlay.setOnClickListener {
            viewModel.startPlayer()
        }
        binding.buttonPause.setOnClickListener {
            viewModel.pausePlayer()
        }
        binding.buttonLike.setOnClickListener {
            viewModel.changeLiked()
        }

        viewModel.textTrackTime.observe(this) {
            binding.textTrackTime.text = it
        }
    }

    private fun statePlay() {
        binding.buttonPlay.visibility = View.GONE
        binding.buttonPause.visibility = View.VISIBLE
    }

    private fun statePause() {
        binding.buttonPlay.visibility = View.VISIBLE
        binding.buttonPause.visibility = View.GONE
    }

    private fun statePrepared() {
        binding.buttonPlay.visibility = View.VISIBLE
        binding.buttonPause.visibility = View.GONE
    }

    private fun stateCompletion() {
        binding.buttonPlay.isEnabled = true
    }

    override fun onStop() {
        viewModel.checkLike()
        super.onStop()
    }

    private fun collectionNameIsEmpty(track: Track): String? {
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
            track.collectionName
        }
    }
}

