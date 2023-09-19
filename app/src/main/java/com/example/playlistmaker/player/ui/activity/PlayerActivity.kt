package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.adapter.PlayerAddToPlaylistAdapter
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.createPlaylist.ui.fragment.CreatePlaylistFragment
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.player.ui.PlayerModelState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity(), PlayerAddToPlaylistAdapter.Listener {

    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: ActivityPlayerBinding
    private var trackId: Int = 0
    private var playlistTitle: String = ""
    private var playlistId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PlayerAddToPlaylistAdapter(this)
        binding.recyclerLayout.adapter = adapter
        binding.recyclerLayout.layoutManager = LinearLayoutManager(this)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        trackId = intent.getIntExtra("trackId", -1)

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

        viewModel.stateForCreatePlaylist.observe(this) {
            if (it) {
                binding.viewPlayer.visibility = View.GONE
                binding.overlay.visibility = View.GONE
                binding.playlistsBottomSheet.visibility = View.GONE
                binding.fragmentContainerView.visibility = View.VISIBLE
            } else {
                binding.viewPlayer.visibility = View.VISIBLE
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                    binding.overlay.visibility = View.GONE
                } else binding.overlay.visibility = View.VISIBLE
                binding.playlistsBottomSheet.visibility = View.VISIBLE
                binding.fragmentContainerView.visibility = View.GONE
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                viewModel.openViewCreatePlaylist(true)
            } else {
                viewModel.getAllPlaylists()
                viewModel.openViewCreatePlaylist(false)
            }
        }


        binding.textTrackNameVariable.isSelected = true
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.buttonPlay.setOnClickListener {
            viewModel.playPlayer()
        }
        binding.buttonPause.setOnClickListener {
            viewModel.pausePlayer()
        }
        binding.buttonLike.setOnClickListener {
            viewModel.changeLiked()
        }
        binding.buttonCreate.setOnClickListener {
            createPlaylist(CreatePlaylistFragment())
        }

        binding.buttonAddToPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.overlay.visibility = View.VISIBLE
        }

        viewModel.resultAdding.observe(this) { result ->
            if (result) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                binding.overlay.visibility = View.GONE
                Toast.makeText(this, "Добавлено в плейлист $playlistTitle", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    this,
                    "Трек уже добавлен в плейлист $playlistTitle",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playlist.collect {
                    adapter.setPlaylist(it)
                }
            }
        }

        viewModel.textTrackTime.observe(this) {
            binding.textTrackTime.text = it
        }
    }

    private fun createPlaylist(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            add(R.id.fragment_container_view, fragment)
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

    override fun onPause() {
        viewModel.checkLike(playlistId)
        viewModel.saveState()
        super.onPause()
    }

    override fun onResume() {
        viewModel.restoreState()
        super.onResume()
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

    override fun onClick(playlist: Playlist) {
        playlistTitle = playlist.title
        viewModel.onClick(trackId, playlist.id!!)
    }
}

