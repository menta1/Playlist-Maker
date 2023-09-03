package com.example.playlistmaker.createPlaylist.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.ui.viewmodel.CreatePlaylistViewModel
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.utils.Constants.NAVIGATE_FROM_PLAYLIST
import com.example.playlistmaker.utils.Constants.TEXT_FOR_TOAST
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment() {

    private val viewModel by viewModel<CreatePlaylistViewModel>()
    private lateinit var binding: FragmentCreatePlaylistBinding
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private var uriPick: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textArgument: String? = arguments?.getString(NAVIGATE_FROM_PLAYLIST)
        if (textArgument != null) {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
                View.GONE
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    uriPick = uri
                    binding.imageView.setImageURI(uri)
                    binding.imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }

        confirmDialog =
            MaterialAlertDialogBuilder(requireActivity()).setTitle(R.string.finish_creating_a_playlist)
                .setMessage(R.string.all_unsaved_data_will_be_lost)
                .setNegativeButton(R.string.complete) { _, _ ->
                    exitFromFragment()
                }.setPositiveButton(R.string.cancel) { _, _ -> }

        binding.addPhotoImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.textInputEditTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.buttonCreate.isEnabled = !s.isNullOrBlank()
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.buttonCreate.setOnClickListener {
            var isPlaceholder = true
            if (uriPick != null) {
                isPlaceholder = false
            }
            viewModel.savePlaylist(
                binding.textInputEditTitle.text.toString(),
                binding.textInputEditDescription.text.toString(),
                uriPick,
                isPlaceholder
            )
            val bundle =
                bundleOf(TEXT_FOR_TOAST to ("Плейлист " + binding.textInputEditTitle.text.toString() + " создан"))

            if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                Toast.makeText(
                    requireActivity(),
                    "Плейлист " + binding.textInputEditTitle.text.toString() + " создан",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
                    View.VISIBLE
                findNavController().navigate(
                    R.id.action_createPlaylistFragment_to_mediatekaFragment, bundle
                )
            }
        }

        viewModel.successAddToDb.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "Успешно добавлено!", Toast.LENGTH_SHORT).show()
                exitFromFragment()
            } else {
                Toast.makeText(context, "Ошибка добавления!", Toast.LENGTH_SHORT).show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitDialog()
                }
            })

        binding.materialToolbar.setNavigationOnClickListener {
            exitDialog()
        }
    }

    fun exitDialog() {
        if (uriPick != null || !binding.textInputEditTitle.text.isNullOrBlank() || !binding.textInputEditDescription.text.isNullOrBlank()) {
            confirmDialog.show()
        } else {
            exitFromFragment()
        }
    }

    private fun exitFromFragment() {
        if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
            requireActivity().supportFragmentManager.popBackStack()
        } else {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
                View.VISIBLE
            try {
                findNavController().navigate(R.id.action_createPlaylistFragment_to_mediatekaFragment)
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }
}