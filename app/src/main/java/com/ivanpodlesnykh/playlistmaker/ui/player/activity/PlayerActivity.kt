package com.ivanpodlesnykh.playlistmaker.ui.player.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.databinding.ActivityPlayerBinding
import com.ivanpodlesnykh.playlistmaker.domain.player.models.PlayerState
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private val viewModel: PlayerViewModel by viewModel<PlayerViewModel> {
        parametersOf(Gson().fromJson(intent.extras!!.getString("track"), Track::class.java).previewUrl)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val track: Track = Gson().fromJson(intent.extras!!.getString("track"), Track::class.java)

        viewModel.getPlayerStateLiveData().observe(this){
            when(it){
                PlayerState.STATE_PREPARED ->{
                    handlePlayPauseButton(true)
                    binding.playerCurrentPlaytime.text =  getString(R.string.current_playtime_default)
                }
                else -> {

                }
            }
        }

        viewModel.getCurrentPlaytimeLiveData().observe(this) {
            binding.playerCurrentPlaytime.text = it
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
            this.finish()
        }
    }

    private fun handlePlayPauseButton(play: Boolean) {
        val nightModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlag == Configuration.UI_MODE_NIGHT_YES) {
            if(play) binding.playerPlayButton.setImageDrawable(getDrawable(R.drawable.player_play_button_night))
            else binding.playerPlayButton.setImageDrawable(getDrawable(R.drawable.player_pause_button_night))
        } else {
            if(play) binding.playerPlayButton.setImageDrawable(getDrawable(R.drawable.player_play_button_daylight))
            else binding.playerPlayButton.setImageDrawable(getDrawable(R.drawable.player_pause_button_daylight))
        }
    }

    private fun handleTrackInfo(track: Track) {
        val extras = intent.extras

        if (extras != null) {
            Glide.with(applicationContext)
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
}