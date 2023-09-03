package com.example.playlistmaker.mediateka.favorite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.adapter.TrackAdapter
import com.example.playlistmaker.databinding.FragmentFavotitesTracksBinding
import com.example.playlistmaker.player.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTracksFragment : Fragment(), TrackAdapter.Listener {

    private val viewModel by viewModel<FavoritesTracksViewModel>()
    private lateinit var binding: FragmentFavotitesTracksBinding

    companion object {
        fun newInstance() = FavoritesTracksFragment()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllTracksFavorite()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavotitesTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TrackAdapter(this)
        binding.recyclerFavoriteSongs.adapter = adapter
        binding.recyclerFavoriteSongs.layoutManager = LinearLayoutManager(requireContext())

        viewModel.viewStateControllerLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is FavoritesTracksUiState.EmptyFavoriteTracks -> emptyFavoriteTracks()
                is FavoritesTracksUiState.HasFavoriteTracks -> hasFavoriteTracks()
            }
        }

        viewModel.favoriteTrackLiveData.observe(viewLifecycleOwner) {
            adapter.setTracks(it)
        }
    }

    private fun emptyFavoriteTracks() {
        binding.imageViewEmpty.visibility = View.VISIBLE
        binding.textViewEmpty.visibility = View.VISIBLE
        binding.recyclerFavoriteSongs.visibility = View.GONE
    }

    private fun hasFavoriteTracks() {
        binding.imageViewEmpty.visibility = View.GONE
        binding.textViewEmpty.visibility = View.GONE
        binding.recyclerFavoriteSongs.visibility = View.VISIBLE
    }

    override fun onClick(track: Track) {
        viewModel.onClick(track)
    }
}