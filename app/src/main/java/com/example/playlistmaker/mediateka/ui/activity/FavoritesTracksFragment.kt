package com.example.playlistmaker.mediateka.ui.activity

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.playlistmaker.mediateka.ui.view_model.FavoritesTracksViewModel
import com.example.playlistmaker.databinding.FragmentFavotitesTracksBinding

class FavoritesTracksFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesTracksFragment()
    }

    private lateinit var binding: FragmentFavotitesTracksBinding
    private val viewModel by viewModels<FavoritesTracksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavotitesTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}