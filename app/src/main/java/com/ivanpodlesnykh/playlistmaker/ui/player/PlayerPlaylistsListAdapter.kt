package com.ivanpodlesnykh.playlistmaker.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist

class PlayerPlaylistsListAdapter(val playlists: List<Playlist>, private val callback: (playlist: Playlist)->Unit) : Adapter<PlayerPlaylistsListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayerPlaylistsListViewHolder {

        return PlayerPlaylistsListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.add_to_playlists_list_item, parent, false), callback)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlayerPlaylistsListViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
}