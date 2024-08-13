package com.example.playlistmaker.mediateka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.mediateka.favorite.ui.FavoritesTracksViewModel
import com.example.playlistmaker.mediateka.playlist.ui.PlaylistViewModel
import com.example.playlistmaker.utils.Constants
import com.example.playlistmaker.utils.Constants.NAVIGATE_FROM_PLAYLIST
import com.example.playlistmaker.utils.Constants.PLAYLIST_ID
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatekaFragment : Fragment() {

    private var toastWasNotShow = true

    private val viewModelFavorite by viewModel<FavoritesTracksViewModel>()
    private val viewModelPlaylist by viewModel<PlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                MediatekaScreen(
                    viewModelFavorite = viewModelFavorite,
                    viewModelPlaylist = viewModelPlaylist,
                    onClickCreatePlaylist = { createNewPlaylist() },
                    onClickCurrentPlaylist = { clickCurrentPlaylist(it) }
                )
            }
        }
    }

    override fun onResume() {
        viewModelPlaylist.getPlaylists()
        viewModelFavorite.getAllTracksFavorite()
        super.onResume()
    }

    private fun showToast(text: String?) {
        if (toastWasNotShow) {
            if (!text.isNullOrEmpty()) {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                toastWasNotShow = false
            }
        }
    }

    private fun createNewPlaylist() {
        val bundle = bundleOf(NAVIGATE_FROM_PLAYLIST to "true")
        findNavController().navigate(
            R.id.action_mediatekaFragment_to_createPlaylistFragment,
            bundle
        )
    }

    private fun clickCurrentPlaylist(playlist: Playlist) {
        val bundle = bundleOf(PLAYLIST_ID to playlist.id, NAVIGATE_FROM_PLAYLIST to "true")
        findNavController().navigate(
            R.id.action_mediatekaFragment_to_currentPlaylistFragment,
            bundle
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textArgument: String? = arguments?.getString(Constants.TEXT_FOR_TOAST)
        showToast(textArgument)
    }
}