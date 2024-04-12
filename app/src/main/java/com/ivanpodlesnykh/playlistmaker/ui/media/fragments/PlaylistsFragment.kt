package com.ivanpodlesnykh.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.ivanpodlesnykh.playlistmaker.databinding.FragmentPlaylistsBinding
import com.ivanpodlesnykh.playlistmaker.domain.media.models.PlaylistsState
import com.ivanpodlesnykh.playlistmaker.ui.media.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel: PlaylistsViewModel by viewModel<PlaylistsViewModel>()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistsState.NoPlaylists -> showNoContent()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showNoContent() {
        binding.newPlaylistButton.isVisible = true
        binding.nothingFoundImage.isVisible = true
        binding.nothingFoundText.isVisible = true
    }

    companion object {
        fun newInstance() = PlaylistsFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}