package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import java.text.SimpleDateFormat
import java.util.*

class AudioplayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioplayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textTrackNameVariable.isSelected = true
        binding.backButton.setOnClickListener {
            finish()
        }
        val track: Track? = intent.extras?.getParcelable("TRACK")
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


}