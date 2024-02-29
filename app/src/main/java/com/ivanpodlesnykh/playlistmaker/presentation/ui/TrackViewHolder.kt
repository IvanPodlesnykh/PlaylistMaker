package com.ivanpodlesnykh.playlistmaker.presentation.ui

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.data.shared_preferences.SearchHistory
import com.ivanpodlesnykh.playlistmaker.domain.models.Track
import com.ivanpodlesnykh.playlistmaker.presentation.ui.player.PlayerActivity
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

class TrackViewHolder(itemView: View) : ViewHolder(itemView) {
    
    private var activatedAt: Long? = null
    
    val songName = itemView.findViewById<TextView>(R.id.track_name)
    val artistName = itemView.findViewById<TextView>(R.id.artist_name)
    val trackTime = itemView.findViewById<TextView>(R.id.track_time)
    val trackCover = itemView.findViewById<ImageView>(R.id.track_cover)

    val sharedPreferences = itemView.context.getSharedPreferences("sharedPref",
        AppCompatActivity.MODE_PRIVATE
    )
    private val searchHistory = SearchHistory(sharedPreferences)

    fun bind(track: Track) {
        songName.text = track.trackName
        artistName.text = track.artistName
        artistName.requestLayout()
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_cover_corner_radius)))
            .into(trackCover)

        itemView.setOnClickListener{
            
            if(activatedAt == null || abs(activatedAt!! - System.currentTimeMillis()) >= DEBOUNCER_TIME) {
                activatedAt = System.currentTimeMillis()

                searchHistory.saveTrackToList(track)

                val playerIntent = Intent(itemView.context, PlayerActivity::class.java)

                val json = Gson().toJson(track)

                playerIntent.putExtra("track", json)

                itemView.context.startActivity(playerIntent)
            }
        }
    }

    companion object {
        const val DEBOUNCER_TIME = 1000L
    }
}