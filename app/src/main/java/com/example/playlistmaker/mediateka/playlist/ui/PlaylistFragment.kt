package com.example.playlistmaker.mediateka.playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.adapter.PlaylistAdapter
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.utils.Constants.NAVIGATE_FROM_PLAYLIST
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment(), PlaylistAdapter.Listener {

    private val viewModel by viewModel<PlaylistViewModel>()
    private lateinit var binding: FragmentPlaylistBinding

    companion object {
        fun newInstance() = PlaylistFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PlaylistAdapter(this)
        binding.recyclerLayout.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerLayout.adapter = adapter

        binding.buttonCreate.setOnClickListener {
            val bundle = bundleOf(NAVIGATE_FROM_PLAYLIST to "true")
            findNavController().navigate(
                R.id.action_mediatekaFragment_to_createPlaylistFragment,
                bundle
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.getPlaylists()
                viewModel.playlist.collect { uiState ->
                    when (uiState) {
                        is PlaylistUiState.HasPlaylists -> {
                            binding.imageView.visibility = View.GONE
                            binding.textView2.visibility = View.GONE
                            binding.recyclerLayout.visibility = View.VISIBLE
                            adapter.setPlaylist(uiState.playlist)
                        }

                        is PlaylistUiState.EmptyPlaylist -> {
                            binding.imageView.visibility = View.VISIBLE
                            binding.textView2.visibility = View.VISIBLE
                            binding.recyclerLayout.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onClick(playlist: Playlist) {

    }
}