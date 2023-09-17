package com.example.playlistmaker.currentPlaylist.ui.currentPlaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.adapter.TrackAdapter
import com.example.playlistmaker.databinding.FragmentCurrentPlaylistBinding
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class CurrentPlaylistFragment : Fragment(), TrackAdapter.Listener,
    TrackAdapterForCurrentPlaylist.LongListener {

    private val viewModel by viewModel<CurrentPlaylistViewModel>()
    private lateinit var binding: FragmentCurrentPlaylistBinding
    private var playlistId: Int? = null
    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        playlistId = arguments?.getInt(Constants.PLAYLIST_ID)
        val textArgument: String? = arguments?.getString(Constants.NAVIGATE_FROM_PLAYLIST)
        if (textArgument != null) {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
                View.GONE
        }
        viewModel.getPlaylist(playlistId!!)
        viewModel.getTracks(playlistId!!)

        binding = FragmentCurrentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.sumTimeAllTracks()

        val bottomSheetBehaviorMore =
            BottomSheetBehavior.from(binding.playlistsMoreBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        binding.imageMore.setOnClickListener {
            bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.overlay.visibility = View.VISIBLE
        }

        binding.textViewEditInfo.setOnClickListener {
            val bundle = bundleOf(
                Constants.PLAYLIST_ID to playlistId
            )
            findNavController().navigate(
                R.id.action_currentPlaylistFragment_to_editPlaylistFragment,
                bundle
            )
        }

        bottomSheetBehaviorMore.addBottomSheetCallback(object :
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

        viewModel.playlist.observe(viewLifecycleOwner) {
            with(binding) {
                textTitle.text = it.title
                playlistItemPlayer.textViewTitle.text = it.title
                if (it.description != "") {
                    textDescription.text = it.description
                } else {
                    textDescription.visibility = View.GONE
                }
                val countTracks = it.countTracks.toString() + " " + changeEnding(it.countTracks)
                textCountTrack.text = countTracks
                playlistItemPlayer.textViewCountTracks.text = countTracks

                Glide.with(binding.root)
                    .load(File(it.filePath))
                    .placeholder(R.drawable.placeholder_high)
                    .centerCrop()
                    .into(binding.imageView)

                Glide.with(binding.root)
                    .load(File(it.filePath))
                    .placeholder(R.drawable.placeholder_high)
                    .centerCrop()
                    .transform(RoundedCorners(binding.root.resources.getDimensionPixelSize(R.dimen.rounded_artworkUrl100_high)))
                    .into(binding.playlistItemPlayer.imageView)

            }
        }

        val adapter = TrackAdapterForCurrentPlaylist(this, this)

        binding.recyclerLayout.adapter = adapter
        binding.recyclerLayout.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tracks.collect {
                if (it.isEmpty()) {
                    Toast.makeText(
                        requireActivity(),
                        "В этом плейлисте нет треков",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                adapter.setTracks(it)
            }
        }

        binding.shareButton.setOnClickListener {
            share()
        }
        binding.textViewShare.setOnClickListener {
            share()
            bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.backButton.setOnClickListener {
            navToMediatekaPlaylist()
        }

        viewModel.time.observe(viewLifecycleOwner) {
            binding.textTime.text = formatTime(it)
        }

        binding.textViewDeletePlaylist.setOnClickListener {
            confirmDialog =
                MaterialAlertDialogBuilder(requireActivity()).setTitle(R.string.delete_playlist)
                    .setMessage("Хотите удалить плейлист?")
                    .setNegativeButton("Да") { _, _ ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.deletePlaylist()
                            navToMediatekaPlaylist()
                        }
                    }.setPositiveButton("Нет") { _, _ -> }
            confirmDialog.show()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navToMediatekaPlaylist()
                }
            })
        binding.textTitle.isSelected = true
        binding.textDescription.isSelected = true
    }

    override fun onResume() {
        viewModel.updatePlaylist()
        super.onResume()
    }

    override fun onClick(track: Track) {
        viewModel.onClick(track)
    }

    override fun onLongClick(track: Track) {
        confirmDialog =
            MaterialAlertDialogBuilder(requireActivity()).setTitle(R.string.delete_track)
                .setMessage(R.string.sure_delete_track)
                .setNegativeButton(R.string.delete) { _, _ ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.deleteTrack(track, playlistId!!)
                    }
                }.setPositiveButton(R.string.cancel) { _, _ -> }
        confirmDialog.show()
    }

    private fun changeEnding(count: Int): String {
        val lastDigit = count % 10
        val lastTwoDigits = count % 100

        return when {
            lastTwoDigits in 11..19 -> "треков"
            lastDigit == 1 -> "трек"
            lastDigit in 2..4 -> "трека"
            else -> "треков"
        }
    }

    private fun minuteEnding(minute: Int): String {
        val lastDigit = minute % 10
        val lastTwoDigits = minute % 100

        return when {
            lastTwoDigits in 11..19 -> "минут"
            lastDigit == 1 -> "минута"
            lastDigit in 2..4 -> "минуты"
            else -> "минут"
        }
    }

    private fun formatTime(minute: Int): String {
        return if (minute != 0) {
            val time = SimpleDateFormat(
                "mm",
                Locale.getDefault()
            ).format(minute).trimStart('0')
            time + " " + minuteEnding(time.toInt())
        } else {
            "0 минут"
        }
    }

    private fun navToMediatekaPlaylist() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.VISIBLE
        val bundle =
            bundleOf(Constants.RESULT_NAV_CREATE_OR_CURRENT_PLAYLIST to true)
        findNavController().navigate(
            R.id.action_currentPlaylistFragment_to_mediatekaFragment,
            bundle
        )
    }

    private fun share() {
        val result = viewModel.share()
        if (!result) {
            Toast.makeText(
                requireActivity(),
                "В этом плейлисте нет списка треков, которым можно поделиться",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}