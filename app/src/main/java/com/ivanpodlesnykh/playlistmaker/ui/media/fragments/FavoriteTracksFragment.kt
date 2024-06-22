package com.ivanpodlesnykh.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.ivanpodlesnykh.playlistmaker.domain.media.models.FavoriteTracksState
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.ui.media.view_model.FavoriteTracksViewModel
import com.ivanpodlesnykh.playlistmaker.ui.search.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private val viewModel: FavoriteTracksViewModel by viewModel<FavoriteTracksViewModel>()

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is FavoriteTracksState.NoContent -> showNoContent()
                is FavoriteTracksState.ShowContent -> showContent(it.listOfFavoriteTracks)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        viewModel.updateState()
    }

    private fun showNoContent() {
        binding.nothingFoundImage.isVisible = true
        binding.nothingFoundText.isVisible = true

        binding.listOfTracks.isVisible = false
    }

    private fun showContent(listOfTracks: List<Track>) {
        binding.nothingFoundImage.isVisible = false
        binding.nothingFoundText.isVisible = false

        binding.listOfTracks.isVisible = true

        val adapter = TrackAdapter(listOfTracks, lifecycleScope, findNavController(), R.id.action_mediaFragment_to_playerFragment)

        binding.listOfTracks.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}