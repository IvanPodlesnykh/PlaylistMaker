package com.ivanpodlesnykh.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : ViewHolder(itemView) {

    fun bind(track: Track) {
        val songName = itemView.findViewById<TextView>(R.id.song_name)
        val artistName = itemView.findViewById<TextView>(R.id.artist_name)
        val trackTime = itemView.findViewById<TextView>(R.id.track_time)
        val trackCover = itemView.findViewById<ImageView>(R.id.track_cover)

        songName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime

        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_cover_corner_radius)))
            .into(trackCover)
    }
}