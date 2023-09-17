package com.example.playlistmaker.currentPlaylist.ui.editPlaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.ui.fragment.CreatePlaylistFragment
import com.example.playlistmaker.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class EditPlaylistFragment : CreatePlaylistFragment() {

    override val viewModel by viewModel<EditPlaylistViewModel>()
    private var playlistId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        playlistId = arguments?.getInt(Constants.PLAYLIST_ID)
        viewModel.getPlaylist(playlistId!!)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.materialToolbar.title = "Редактировать"
        binding.buttonCreate.text = "Сохранить"
        viewModel.playlist.observe(viewLifecycleOwner) {
            with(binding) {
                textInputEditTitle.setText(it.title)
                textInputLayout.requestFocus()
                textInputEditDescription.setText(it.description)
                textInputDescription.requestFocus()
                Glide.with(binding.root).load(File(it.filePath))
                    .placeholder(R.drawable.placeholder_high).centerCrop()
                    .transform(RoundedCorners(binding.root.resources.getDimensionPixelSize(R.dimen.rounded_artworkUrl100_high)))
                    .into(binding.imageView)
            }
        }

        binding.buttonCreate.setOnClickListener {
            viewModel.updatePlaylistFields(
                title = binding.textInputEditTitle.text.toString(),
                description = binding.textInputEditDescription.text.toString(), uriPick = uriPick
            )
        }
        viewModel.playlistUpdated.observe(viewLifecycleOwner) {
            exitFromFragment()
        }
        binding.materialToolbar.setNavigationOnClickListener {
            exitFromFragment()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitFromFragment()
                }
            })

    }

    override fun exitFromFragment() {
        val bundle = bundleOf(
            Constants.PLAYLIST_ID to playlistId,
            Constants.RESULT_NAV_CREATE_OR_CURRENT_PLAYLIST to true
        )
        findNavController().navigate(
            R.id.action_editPlaylistFragment_to_currentPlaylistFragment, bundle
        )
    }
}