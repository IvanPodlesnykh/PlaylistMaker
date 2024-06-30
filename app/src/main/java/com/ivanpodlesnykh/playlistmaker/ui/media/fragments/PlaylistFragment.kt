package com.ivanpodlesnykh.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.databinding.FragmentPlaylistBinding
import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.ui.media.TrackInPlaylistAdapter
import com.ivanpodlesnykh.playlistmaker.ui.media.view_model.PlaylistViewModel
import com.ivanpodlesnykh.playlistmaker.utils.UtilFunctions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModel<PlaylistViewModel> {
        parametersOf(requireArguments().getLong(PlaylistsFragment.PLAYLIST_KEY))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        viewModel.getPlaylist()

        viewModel.getTrackList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackListBehavior = BottomSheetBehavior.from(binding.bottomSheetTrackList)

        val displayMetrics = requireContext().resources.displayMetrics

        val height = displayMetrics.heightPixels - displayMetrics.widthPixels - 500

        trackListBehavior.peekHeight = height

        val additionalMenuBehavior = BottomSheetBehavior.from(binding.additionalMenu)

        additionalMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        additionalMenuBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1f
            }
        })

        binding.moreButton.setOnClickListener {
            additionalMenuBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.shareButton.setOnClickListener {
            sharePlaylist()
        }

        binding.shareButtonMenu.setOnClickListener {
            sharePlaylist()
        }

        binding.editInfo.setOnClickListener {
            val playlist = viewModel.getPlaylistLiveData().value
            findNavController().navigate(R.id.action_playlistFragment_to_editPlaylistFragment, bundleOf("PLAYLIST" to Gson().toJson(playlist)))
        }

        binding.deletePlaylist.setOnClickListener {
            deletePlaylist()
        }

        viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) {
            initializePlaylist(it)
        }

        viewModel.getListOfTracksLiveData().observe(viewLifecycleOwner) {
            updatePlaytimeAndNumberOfTracks(it)
            updateTrackList(it)
        }

    }

    private fun initializePlaylist(playlist: Playlist) {

        binding.playlistTitle.text = playlist.title

        binding.playlistTitleMenu.text = playlist.title

        binding.playlistDescription.text = playlist.description

        Glide.with(requireContext())
            .load(playlist.imageUri)
            .placeholder(R.drawable.track_placeholder)
            .transform(CenterCrop())
            .into(binding.playlistCover)

        Glide.with(requireContext())
            .load(playlist.imageUri)
            .placeholder(R.drawable.track_placeholder)
            .transform(CenterCrop(),  RoundedCorners(UtilFunctions().dpToPx(2f, requireContext())))
            .into(binding.playlistCoverMenu)
    }

    private fun updatePlaytimeAndNumberOfTracks(listOfTracks: List<Track>) {

        if(listOfTracks.isEmpty()) binding.playlistTime.text = "0 минут"
        else {
            var time = 0L

            for(track in listOfTracks) time += track.trackTimeMillis

            val timeInMinutes = (time / 60000).toInt()

            binding.playlistTime.text = requireContext().resources.getQuantityString(R.plurals.minutes_plurals, timeInMinutes, timeInMinutes)
        }

        binding.numberOfTracks.text = requireContext().resources.getQuantityString(R.plurals.tracks_plurals, listOfTracks.size, listOfTracks.size)
        binding.numberOfTracksMenu.text = requireContext().resources.getQuantityString(R.plurals.tracks_plurals, listOfTracks.size, listOfTracks.size)
    }

    private fun updateTrackList(listOfTracks: List<Track>) {
        if(listOfTracks.isEmpty()) {

            binding.noTracksInPlaylist.isVisible = true

            val adapter = TrackInPlaylistAdapter(emptyList(), object : TrackInPlaylistAdapter.Callback {
                override fun onClick(track: Track) {
                }

                override fun onHold(track: Track): Boolean {
                    return true
                }
            })

            binding.listOfTracks.adapter = adapter
        }
        else {

            binding.noTracksInPlaylist.isVisible = false

            val adapter = TrackInPlaylistAdapter(listOfTracks.reversed(), object : TrackInPlaylistAdapter.Callback {
                override fun onClick(track: Track) {

                    val json = Gson().toJson(track)

                    findNavController().navigate(R.id.action_playlistFragment_to_playerFragment, bundleOf("TRACK" to json))

                }

                override fun onHold(track: Track): Boolean {

                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(requireContext().getString(R.string.do_you_wish_to_delete_track))
                        .setNegativeButton(requireContext().getString(R.string.no)) { _, _ ->
                        }
                        .setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                            viewModel.deleteTrack(requireArguments().getLong(PlaylistsFragment.PLAYLIST_KEY), track.trackId)
                        }
                        .show()
                    return true
                }

            })

            binding.listOfTracks.adapter = adapter

            adapter.notifyDataSetChanged()
        }
    }

    private fun sharePlaylist() {
        if(viewModel.getListOfTracksLiveData().value!!.isEmpty()) Toast.makeText(requireContext(), resources.getString(R.string.no_tracks), Toast.LENGTH_LONG).show()
        else viewModel.sharePlaylist()
    }

    private fun deletePlaylist() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Хотите удалить плейлист \"${viewModel.getPlaylistLiveData().value!!.title}\"?")
            .setNegativeButton(requireContext().getString(R.string.no)) { _, _ ->

            }
            .setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                viewModel.deletePlaylist()
                lifecycleScope.launch {
                    delay(500)
                    findNavController().navigateUp()
                }
            }
            .show()
    }

}