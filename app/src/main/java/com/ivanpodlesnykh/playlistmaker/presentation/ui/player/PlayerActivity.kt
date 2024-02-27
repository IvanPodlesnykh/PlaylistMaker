package com.ivanpodlesnykh.playlistmaker.presentation.ui.player

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.ivanpodlesnykh.playlistmaker.Creator
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.domain.api.PlayerInteractor
import com.ivanpodlesnykh.playlistmaker.domain.models.PlayerState
import com.ivanpodlesnykh.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView

    private lateinit var playButton: ImageView

    private lateinit var currentTrackTime : TextView

    private lateinit var url: String

    private var mainThreadHandler: Handler? = null

    private lateinit var playerInteractor: PlayerInteractor

    private val updateTime = object : Runnable {
        override fun run() {
            mainThreadHandler?.post {
                currentTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(playerInteractor.getCurrentPlaytime())
                mainThreadHandler?.postDelayed(this, UPDATE_TIME)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        mainThreadHandler = Handler(Looper.getMainLooper())

        currentTrackTime = findViewById(R.id.player_current_playtime)

        handleTrackInfo()

        playerInteractor = Creator.getPlayerInteractor()

        playerInteractor.preparePlayer(url) {
            handlePlayPauseButton(true)
            currentTrackTime.text = getString(R.string.current_playtime_default)
        }

        handleButtons()
    }

    private fun handleButtons() {
        playButton = findViewById(R.id.player_play_button)

        handlePlayPauseButton(true)

        playButton.setOnClickListener {
            playbackControl()
        }

        backButton = findViewById(R.id.player_back_button)

        backButton.setOnClickListener{
            this.finish()
        }
    }

    private fun handlePlayPauseButton(play: Boolean) {
        val nightModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlag == Configuration.UI_MODE_NIGHT_YES) {
            if(play) playButton.setImageDrawable(getDrawable(R.drawable.player_play_button_night))
            else playButton.setImageDrawable(getDrawable(R.drawable.player_pause_button_night))
        } else {
            if(play) playButton.setImageDrawable(getDrawable(R.drawable.player_play_button_daylight))
            else playButton.setImageDrawable(getDrawable(R.drawable.player_pause_button_daylight))
        }
    }

    private fun handleTrackInfo() {
        val trackCover = findViewById<ImageView>(R.id.player_cover_artwork)
        val trackName = findViewById<TextView>(R.id.player_track_name)
        val artistName = findViewById<TextView>(R.id.player_artist_name)
        val trackPlaytime = findViewById<TextView>(R.id.track_info_playtime)
        val releaseDate = findViewById<TextView>(R.id.track_info_year)
        val primaryGenreName = findViewById<TextView>(R.id.track_info_genre)
        val country = findViewById<TextView>(R.id.track_info_country)
        val collectionName = findViewById<TextView>(R.id.track_info_album)
        val collectionNameTitle = findViewById<TextView>(R.id.track_info_album_name)

        val extras = intent.extras

        if (extras != null) {
            val track: Track = Gson().fromJson(extras.getString("track"), Track::class.java)

            Glide.with(this)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.player_track_cover_placeholder)
                .fitCenter()
                .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.PlayerArtworkCornerRadius)))
                .into(trackCover)

            trackName.text = track.trackName
            artistName.text = track.artistName
            releaseDate.text = track.releaseDate.substring(0, 4)
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country

            currentTrackTime.text = getString(R.string.current_playtime_default)

            trackPlaytime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

            if(track.collectionName != "") {
                collectionName.isVisible = true
                collectionNameTitle.isVisible = true
                collectionName.text = track.collectionName
            }
            else {
                collectionName.isVisible = false
                collectionNameTitle.isVisible = false
            }

            url = track.previewUrl
        }
    }

    override fun onPause() {
        mainThreadHandler?.removeCallbacks(updateTime)
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.stopPlayer()
    }

    private fun playbackControl() {
        when(playerInteractor.getPlayerState()) {
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
        playerInteractor.playMusic()
        handlePlayPauseButton(false)
        mainThreadHandler?.post(updateTime)
    }

    private fun pausePlayer() {
        mainThreadHandler?.removeCallbacks(updateTime)
        playerInteractor.pauseMusic()
        handlePlayPauseButton(true)
    }

    companion object {
        private const val UPDATE_TIME = 200L
    }
}