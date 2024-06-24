package com.ivanpodlesnykh.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.databinding.FragmentPlaylistsBinding
import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist
import com.ivanpodlesnykh.playlistmaker.domain.media.models.PlaylistsState
import com.ivanpodlesnykh.playlistmaker.ui.media.PlaylistsListAdapter
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
                is PlaylistsState.Content -> showContent(it.playlists)
            }
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment)
        }

    }

    override fun onResume() {
        super.onResume()

        viewModel.getPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showNoContent() {
        binding.nothingFoundImage.isVisible = true
        binding.nothingFoundText.isVisible = true
        binding.playlistsList.isVisible = false
    }

    private fun showContent(playlists: List<Playlist>) {
        binding.nothingFoundImage.isVisible = false
        binding.nothingFoundText.isVisible = false

        binding.playlistsList.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = PlaylistsListAdapter(playlists) {
            findNavController().navigate(R.id.action_mediaFragment_to_playlistFragment, bundleOf(
                PLAYLIST_KEY to it))
        }

        binding.playlistsList.adapter = adapter

        adapter.notifyDataSetChanged()

        binding.playlistsList.isVisible = true
    }

    companion object {

        const val PLAYLIST_KEY = "PLAYLIST_KEY"

        fun newInstance() = PlaylistsFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}