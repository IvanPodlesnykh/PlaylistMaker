package com.ivanpodlesnykh.playlistmaker.ui.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist

class PlaylistsListAdapter(val playlists: List<Playlist>) : Adapter<PlaylistsListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsListViewHolder {

        return PlaylistsListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlists_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistsListViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
}