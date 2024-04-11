package com.ivanpodlesnykh.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.ivanpodlesnykh.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.ivanpodlesnykh.playlistmaker.domain.media.models.FavoriteTracksState
import com.ivanpodlesnykh.playlistmaker.ui.media.view_model.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private val viewModel: FavoriteTracksViewModel by viewModel<FavoriteTracksViewModel>()

    private lateinit var binding: FragmentFavoriteTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is FavoriteTracksState.NoContent -> showNoContent()
            }
        }
    }

    private fun showNoContent() {
        binding.nothingFoundImage.isVisible = true
        binding.nothingFoundText.isVisible = true
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}