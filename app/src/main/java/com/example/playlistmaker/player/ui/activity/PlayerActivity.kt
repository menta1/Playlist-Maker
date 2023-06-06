package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.player.ui.view_model.PlayerViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, PlayerViewModelFactory(this))[PlayerViewModel::class.java]

        binding.textTrackNameVariable.isSelected = true
        binding.backButton.setOnClickListener {
            finish()
        }

        with(binding) {
            textTrackNameVariable.text = viewModel.textTrackNameVariable.value?.trackName
            textArtistNameVariable.text = viewModel.textTrackNameVariable.value?.artistName
            textTrackTimeMillisVariable.text = (SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(viewModel.textTrackNameVariable.value?.trackTimeMillis)).toString()
            textCollectionNameVariable.text =
                viewModel.textTrackNameVariable.value?.let { collectionNameIsEmpty() }
            textReleaseDateVariable.text =
                viewModel.textTrackNameVariable.value?.releaseDate?.substring(0, 4)
            textPrimaryGenreNameVariable.text =
                viewModel.textTrackNameVariable.value?.primaryGenreName
            textCountryVariable.text = viewModel.textTrackNameVariable.value?.country
        }
        Glide.with(binding.root)
            .load(
                viewModel.textTrackNameVariable.value?.artworkUrl100?.replaceAfterLast(
                    '/',
                    "512x512bb.jpg"
                )
            )
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

        viewModel.playerState.observe(this) {
            when (viewModel.playerState.value) {
                PlayerState.STATE_PREPARED -> {
                    binding.buttonPlay.visibility = View.VISIBLE
                    binding.buttonPause.visibility = View.GONE
                }

                PlayerState.STATE_COMPLETION -> {
                    binding.buttonPlay.isEnabled = true
                }

                else -> {}
            }
        }
        viewModel.textTrackTime.observe(this) {
            binding.textTrackTime.text = viewModel.textTrackTime.value
        }
    }

    private fun playbackControl() {
        when (viewModel.playerState.value) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        pausePlayer()
        viewModel.mediaPlayerRelease()
    }

    private fun collectionNameIsEmpty(): String {
        return if (viewModel.textTrackNameVariable.value?.collectionName?.isEmpty() == true) {
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
            viewModel.textTrackNameVariable.value?.collectionName.toString()
        }
    }

    private fun startPlayer() {
        viewModel.startPlayer()
        binding.buttonPlay.visibility = View.GONE
        binding.buttonPause.visibility = View.VISIBLE
    }

    private fun pausePlayer() {
        viewModel.pausePlayer()
        binding.buttonPlay.visibility = View.VISIBLE
        binding.buttonPause.visibility = View.GONE
    }
}

