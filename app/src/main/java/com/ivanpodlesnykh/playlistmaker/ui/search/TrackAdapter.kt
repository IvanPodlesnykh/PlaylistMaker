package com.ivanpodlesnykh.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.CoroutineScope

class TrackAdapter(val trackList: List<Track>, private val coroutineScope: CoroutineScope) : Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false), coroutineScope)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }
}