package com.ivanpodlesnykh.playlistmaker.ui.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.data.search.shared_preferences.SearchHistory
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View, private val coroutineScope: CoroutineScope, private val navController: NavController, private val destination: Int) : ViewHolder(itemView) {
    
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

            itemView.isEnabled = false

            coroutineScope.launch {
                delay(DEBOUNCER_TIME)
                itemView.isEnabled = true
            }

            searchHistory.saveTrackToList(track)

            val json = Gson().toJson(track)

            navController.navigate(destination,
                bundleOf("TRACK" to json)
            )
        }
    }

    companion object {
        const val DEBOUNCER_TIME = 1000L
    }
}