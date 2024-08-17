package com.example.playlistmaker.search.ui.activity

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.playlistmaker.search.ui.SearchScreen
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.utils.InternetCheckReceiver
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()

    private val internetCheckReceiver by lazy { InternetCheckReceiver() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SearchScreen(
                    viewModel = viewModel
                )
            }
        }
    }

    override fun onResume() {
        ContextCompat.registerReceiver(
            this.requireContext(),
            internetCheckReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        super.onResume()
    }

    override fun onPause() {
        activity?.unregisterReceiver(internetCheckReceiver)
        super.onPause()
    }
}