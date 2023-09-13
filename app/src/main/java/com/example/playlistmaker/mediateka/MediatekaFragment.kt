package com.example.playlistmaker.mediateka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediatekaBinding
import com.example.playlistmaker.utils.Constants
import com.google.android.material.tabs.TabLayoutMediator

class MediatekaFragment : Fragment() {
    private lateinit var binding: FragmentMediatekaBinding
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private var toastWasNotShow = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediatekaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textArgument: String? = arguments?.getString(Constants.TEXT_FOR_TOAST)
        var returnFromCreatePlaylist: Boolean? =
            arguments?.getBoolean(Constants.RESULT_NAV_CREATE_OR_CURRENT_PLAYLIST)

        showToast(textArgument)

        binding.viewPager.adapter = MediatekaPagerAdapter(childFragmentManager, lifecycle)

        tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.favorites_tracks)
                    1 -> tab.text = getString(R.string.playlist)
                }
            }
        tabLayoutMediator.attach()
        if (returnFromCreatePlaylist != null) {
            binding.viewPager.setCurrentItem(1, false)
            returnFromCreatePlaylist = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator.detach()
    }

    private fun showToast(text: String?) {
        if (toastWasNotShow) {
            if (!text.isNullOrEmpty()) {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                toastWasNotShow = false
            }
        }
    }
}