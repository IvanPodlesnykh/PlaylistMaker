package com.ivanpodlesnykh.playlistmaker.ui.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.ui.search.TrackViewHolder

class TrackInPlaylistAdapter(val listOfTracks: List<Track>, private val callback: Callback) : Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listOfTracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            callback.onClick(listOfTracks[position])
        }

        holder.itemView.setOnLongClickListener{
            callback.onHold(listOfTracks[position])
        }

        holder.bind(listOfTracks[position])
    }

    interface Callback {
        fun onClick(track: Track)

        fun onHold(track: Track): Boolean
    }
}