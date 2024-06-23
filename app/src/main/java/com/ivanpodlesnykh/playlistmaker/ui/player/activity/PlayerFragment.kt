package com.ivanpodlesnykh.playlistmaker.ui.player.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.databinding.FragmentPlayerBinding
import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist
import com.ivanpodlesnykh.playlistmaker.domain.player.models.AddTrackToPlaylistState
import com.ivanpodlesnykh.playlistmaker.domain.player.models.PlayerState
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.ui.player.PlayerPlaylistsListAdapter
import com.ivanpodlesnykh.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: PlayerViewModel by viewModel<PlayerViewModel> {
        parametersOf(Gson().fromJson(requireArguments().getString(TRACK_KEY), Track::class.java))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = Gson().fromJson(requireArguments().getString(TRACK_KEY), Track::class.java)

        viewModel.getPlayerStateLiveData().observe(viewLifecycleOwner){
            when(it){
                PlayerState.STATE_PREPARED ->{
                    handlePlayPauseButton(true)
                    binding.playerCurrentPlaytime.text =  getString(R.string.current_playtime_default)
                }
                else -> {

                }
            }
        }

        viewModel.getCurrentPlaytimeLiveData().observe(viewLifecycleOwner) {
            binding.playerCurrentPlaytime.text = it
        }

        val currentTheme = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        viewModel.getFavoriteTrackLiveData().observe(viewLifecycleOwner) {
            if(it) {
                if (currentTheme){
                    binding.playerLikeButton.setImageResource(R.drawable.player_like_button_active_night)
                } else {
                    binding.playerLikeButton.setImageResource(R.drawable.player_like_button_active)
                }
            } else {
                if (currentTheme){
                    binding.playerLikeButton.setImageResource(R.drawable.player_like_button_night)
                } else {
                    binding.playerLikeButton.setImageResource(R.drawable.player_like_button)
                }
            }
        }

        var bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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

        viewModel.getPlaylistsLiveData().observe(viewLifecycleOwner) {
            showPlaylists(it)
        }

        viewModel.getAddTrackToPlaylistStatusLiveData().observe(viewLifecycleOwner) {
            when(it) {
                is AddTrackToPlaylistState.Default -> {

                }
                is AddTrackToPlaylistState.TrackAdded -> {
                    Toast.makeText(requireContext(), "Добавлено в плейлист ${it.playlistTitle}", Toast.LENGTH_LONG).show()

                    BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
                }
                is AddTrackToPlaylistState.TrackIsNotAdded -> {
                    Toast.makeText(requireContext(), "Трек уже добавлен в плейлист ${it.playlistTitle}", Toast.LENGTH_LONG).show()
                }
            }
        }

        handleTrackInfo(track)

        handleButtons()
    }

    private fun handleButtons() {
        handlePlayPauseButton(true)

        binding.playerPlayButton.setOnClickListener {
            playbackControl()
        }

        binding.playerBackButton.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.playerLikeButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        binding.playerAddToPlaylistButton.setOnClickListener {
            BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            viewModel.getPlaylists()
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_createPlaylistFragment)
        }
    }

    private fun handlePlayPauseButton(play: Boolean) {
        val nightModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlag == Configuration.UI_MODE_NIGHT_YES) {
            if(play) binding.playerPlayButton.setImageDrawable(requireContext().getDrawable(R.drawable.player_play_button_night))
            else binding.playerPlayButton.setImageDrawable(requireContext().getDrawable(R.drawable.player_pause_button_night))
        } else {
            if(play) binding.playerPlayButton.setImageDrawable(requireContext().getDrawable(R.drawable.player_play_button_daylight))
            else binding.playerPlayButton.setImageDrawable(requireContext().getDrawable(R.drawable.player_pause_button_daylight))
        }
    }

    private fun handleTrackInfo(track: Track) {
        Glide.with(requireContext())
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.player_track_cover_placeholder)
            .fitCenter()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.PlayerArtworkCornerRadius)))
            .into(binding.playerCoverArtwork)

        binding.playerTrackName.text = track.trackName
        binding.playerArtistName.text = track.artistName
        binding.trackInfoYear.text = track.releaseDate.substring(0, 4)
        binding.trackInfoGenre.text = track.primaryGenreName
        binding.trackInfoCountry.text = track.country

        binding.playerCurrentPlaytime.text = getString(R.string.current_playtime_default)

        binding.trackInfoPlaytime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        if(track.collectionName != "") {
            binding.trackInfoAlbum.isVisible = true
            binding.trackInfoAlbumName.isVisible = true
            binding.trackInfoAlbum.text = track.collectionName
        }
        else {
            binding.trackInfoAlbum.isVisible = false
            binding.trackInfoAlbumName.isVisible = false
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    private fun playbackControl() {
        when(viewModel.getPlayerStateLiveData().value) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        viewModel.play()
        handlePlayPauseButton(false)
    }

    private fun pausePlayer() {
        viewModel.pause()
        handlePlayPauseButton(true)
    }

    private fun showPlaylists(playlists: List<Playlist>) {

        val adapter = PlayerPlaylistsListAdapter(playlists) {
            viewModel.addTrackToPlaylist(it)
        }

        binding.playlistsList.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    companion object {
        private const val TRACK_KEY = "TRACK"
    }
}