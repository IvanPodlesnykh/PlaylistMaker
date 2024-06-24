package com.ivanpodlesnykh.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist
import com.ivanpodlesnykh.playlistmaker.ui.media.view_model.EditPlaylistViewModel
import com.ivanpodlesnykh.playlistmaker.utils.UtilFunctions
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment : CreatePlaylistFragment() {

    override val viewModel by viewModel<EditPlaylistViewModel>{

        val playlist = Gson().fromJson(requireArguments().getString("PLAYLIST"), Playlist::class.java)

        parametersOf(playlist)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createButton.text = resources.getString(R.string.save)
        binding.fragmentTitle.text = resources.getString(R.string.edit)

        val playlist = Gson().fromJson(requireArguments().getString("PLAYLIST"), Playlist::class.java)

        binding.playlistTitle.setText(playlist.title)
        binding.playlistDescription.setText(playlist.description)

        Glide.with(requireContext())
            .load(playlist.imageUri)
            .transform(CenterCrop(), RoundedCorners(UtilFunctions().dpToPx(8f, requireContext())))
            .into(binding.playlistCover)
    }

    override fun navigateUpOrConfirm() {
        findNavController().navigateUp()
    }

    override fun showToast() {

    }

}