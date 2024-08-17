package com.example.playlistmaker.player.ui.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.example.playlistmaker.player.ui.MusicService
import com.example.playlistmaker.player.ui.PlaybackState
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.utils.InternetCheckReceiver
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity(), PlayerAddToPlaylistAdapter.Listener {

    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: ActivityPlayerBinding
    private var trackId: Int = 0
    private var trackUrl: String = ""
    private var trackName: String = ""
    private var trackArtist: String = ""
    private var playlistTitle: String = ""
    private var playlistId = 0
    private val internetCheckReceiver = InternetCheckReceiver()
    private var isServiceBind = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicServiceBinder
            viewModel.setAudioPlayerControl(binder.getService())
            isServiceBind = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeAudioPlayerControl()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            bindMusicService()
        } else {
            Toast.makeText(this, "Can't bind service!", Toast.LENGTH_LONG).show()
        }
    }

    private fun bindMusicService() {
        val intent = Intent(this, MusicService::class.java).apply {
            putExtra("song_url", trackUrl)
            putExtra("trackName", trackName)
            putExtra("trackArtist", trackArtist)
        }

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindMusicService() {
        if(isServiceBind) unbindService(serviceConnection)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PlayerAddToPlaylistAdapter(this)
        binding.recyclerLayout.adapter = adapter
        binding.recyclerLayout.layoutManager = LinearLayoutManager(this)

        trackId = intent.getIntExtra("trackId", -1)
        trackUrl = intent.getStringExtra("trackUrl") ?: ""
        trackName = intent.getStringExtra("trackName") ?: ""
        trackArtist = intent.getStringExtra("trackArtist") ?: ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            bindMusicService()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

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
                    "mm:ss", Locale.getDefault()
                ).format(it.trackTimeMillis)).toString()
                textCollectionNameVariable.text = collectionNameIsEmpty(it)
                textReleaseDateVariable.text = it.releaseDate?.substring(0, 4)
                textPrimaryGenreNameVariable.text = it.primaryGenreName
                textCountryVariable.text = it.country
            }
            Glide.with(binding.root).load(
                it.artworkUrl100?.replaceAfterLast(
                    '/', "512x512bb.jpg"
                )
            ).placeholder(R.drawable.placeholder_high).centerCrop()
                .transform(RoundedCorners(binding.root.resources.getDimensionPixelSize(R.dimen.rounded_artworkUrl100_high)))
                .into(binding.placeholderArtworkUrl100)
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
                    this, "Трек уже добавлен в плейлист $playlistTitle", Toast.LENGTH_SHORT
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
        binding.playbackButton.onClickListener = {
            when (binding.playbackButton.getPlaybackState()) {
                PlaybackState.PLAY -> viewModel.onPlayerButtonClicked(false)
                PlaybackState.PAUSE -> viewModel.onPlayerButtonClicked(true)
            }
        }

        viewModel.observePlayerState().observe(this) { state ->
            when (state) {
                is PlayerState.Default -> statePrepared()
                is PlayerState.Paused -> statePause()
                is PlayerState.Playing -> statePlay()
                is PlayerState.Prepared -> stateCompletion()
            }
        }
    }

    private fun statePlay() {
        binding.playbackButton.changeStatePlayer(PlaybackState.PLAY)
    }

    private fun statePause() {
        binding.playbackButton.changeStatePlayer(PlaybackState.PAUSE)
    }

    private fun statePrepared() {
        binding.playbackButton.changeStatePlayer(PlaybackState.PAUSE)
    }

    private fun stateCompletion() {
        binding.playbackButton.isEnabled = true
        binding.playbackButton.changeStatePlayer(PlaybackState.PAUSE)
    }

    private fun createPlaylist(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            add(R.id.fragment_container_view, fragment)
        }
    }

    override fun onPause() {
        viewModel.checkLike(playlistId)
        viewModel.showNotification()
        unregisterReceiver(internetCheckReceiver)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindMusicService()
    }

    override fun onResume() {
        viewModel.hideNotification()
        ActivityCompat.registerReceiver(
            this,
            internetCheckReceiver,
            IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
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

