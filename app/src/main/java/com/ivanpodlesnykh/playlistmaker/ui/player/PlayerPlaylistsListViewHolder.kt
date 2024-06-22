package com.ivanpodlesnykh.playlistmaker.ui.player

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist

class PlayerPlaylistsListViewHolder(private val itemView: View, private val callback: (playlist: Playlist)->Unit) : RecyclerView.ViewHolder(itemView) {

    private val playlistCover = itemView.findViewById<ImageView>(R.id.playerPlaylistCover)
    private val playlistTitle = itemView.findViewById<TextView>(R.id.playerPlaylistTitle)
    private val playlistNumberOfTracks = itemView.findViewById<TextView>(R.id.playerPlaylistNumberOfTracks)

    fun bind(playlist: Playlist) {

        Glide.with(itemView.context)
            .load(playlist.imageUri)
            .placeholder(R.drawable.track_placeholder)
            .transform(CenterCrop(),  RoundedCorners(2))
            .into(playlistCover)

        playlistTitle.text = playlist.title

        playlistNumberOfTracks.text = itemView.context.resources.getQuantityString(R.plurals.tracks_plurals, playlist.numberOfTracks.toInt(), playlist.numberOfTracks.toInt())

        itemView.setOnClickListener {
            callback.invoke(playlist)
        }
    }

}