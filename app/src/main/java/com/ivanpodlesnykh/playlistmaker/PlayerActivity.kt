package com.ivanpodlesnykh.playlistmaker

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        handleTrackInfo()

        handleButtons()

    }

    private fun handleButtons() {
        val backButton = findViewById<ImageView>(R.id.player_back_button)

        backButton.setOnClickListener{
            this.finish()
        }
    }

    private fun handleTrackInfo() {
        val trackCover = findViewById<ImageView>(R.id.player_cover_artwork)
        val trackName = findViewById<TextView>(R.id.player_track_name)
        val artistName = findViewById<TextView>(R.id.player_artist_name)
        val currentPlaytime = findViewById<TextView>(R.id.player_current_playtime)
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

            //change it later to show current playtime!
            currentPlaytime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

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

        }
    }
}