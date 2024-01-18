package com.ivanpodlesnykh.playlistmaker

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : ViewHolder(itemView) {
    val songName = itemView.findViewById<TextView>(R.id.track_name)
    val artistName = itemView.findViewById<TextView>(R.id.artist_name)
    val trackTime = itemView.findViewById<TextView>(R.id.track_time)
    val trackCover = itemView.findViewById<ImageView>(R.id.track_cover)

    val sharedPreferences = itemView.context.getSharedPreferences("sharedPref",
        AppCompatActivity.MODE_PRIVATE
    )
    val searchHistory = SearchHistory(sharedPreferences)

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
            searchHistory.saveTrackToList(track)
        }
    }
}