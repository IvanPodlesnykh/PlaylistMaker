package com.ivanpodlesnykh.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.ivanpodlesnykh.playlistmaker.ui.media.view_model.CreatePlaylistViewModel
import com.ivanpodlesnykh.playlistmaker.utils.UtilFunctions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

open class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    val binding
        get() = _binding!!

    open val viewModel by viewModel<CreatePlaylistViewModel>()

    private val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            uri ->
        if (uri != null) {

            viewModel.saveImageUri(uri.toString())

            viewModel.setImage(true)

            Glide.with(requireContext())
                .load(uri)
                .transform(CenterCrop(), RoundedCorners(UtilFunctions().dpToPx(8f, requireContext())))
                .into(binding.playlistCover)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTitleIsSetLiveData().observe(viewLifecycleOwner) {
            binding.createButton.isEnabled = it
            binding.playlistTitle.isActivated = it
            binding.playlistTitleTopHint.isVisible = it
        }

        viewModel.getDescriptionIsSetLiveData().observe(viewLifecycleOwner) {
            binding.playlistDescription.isActivated = it
            binding.playlistDescriptionTopHint.isVisible = it
        }

        handleButtons()
        handleEditTexts()
    }

    private fun handleButtons() {

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateUpOrConfirm()
                }

            })

        binding.backButton.setOnClickListener {
            navigateUpOrConfirm()
        }

        binding.playlistCover.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.ImageOnly
            ))
        }

        binding.createButton.setOnClickListener {
            viewModel.createPlaylist(
                title = binding.playlistTitle.text.toString(),
                description = binding.playlistDescription.text.toString()
            )

            showToast()

            lifecycleScope.launch {
                delay(500)
                findNavController().navigateUp()
            }
        }
    }

    private fun handleEditTexts() {
        binding.playlistTitle.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(text.isNullOrEmpty()) viewModel.setTitle(false)
                    else viewModel.setTitle(true)
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            }
        )

        binding.playlistDescription.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(text.isNullOrEmpty()) viewModel.setDescription(false)
                    else viewModel.setDescription(true)
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            }
        )
    }

    open fun navigateUpOrConfirm() {
        if (viewModel.getImageIsSetLiveData().value!! or
            viewModel.getTitleIsSetLiveData().value!! or
            viewModel.getDescriptionIsSetLiveData().value!!) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(requireContext().getString(R.string.close_playlist_creator_dialog_title))
                .setMessage(requireContext().getString(R.string.close_playlist_creator_dialog_message))
                .setNeutralButton(requireContext().getString(R.string.cancel)) { _, _ ->
                }
                .setPositiveButton(requireContext().getString(R.string.finish)) { _, _ ->
                    findNavController().navigateUp()
                }
                .show()
        } else {
            findNavController().navigateUp()
        }
    }

    open fun showToast() {
        Toast.makeText(requireContext(), "Плейлист ${binding.playlistTitle.text} создан", Toast.LENGTH_LONG).show()
    }
}