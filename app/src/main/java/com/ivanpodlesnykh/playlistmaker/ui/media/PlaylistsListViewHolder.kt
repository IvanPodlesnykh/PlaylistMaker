package com.ivanpodlesnykh.playlistmaker.ui.media

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist
import com.ivanpodlesnykh.playlistmaker.utils.UtilFunctions

class PlaylistsListViewHolder(private val itemView: View, private val onClick: (playlistId: Long) -> Unit) : ViewHolder(itemView) {

    private val playlistCover = itemView.findViewById<ImageView>(R.id.playlistCover)
    private val playlistTitle = itemView.findViewById<TextView>(R.id.playlistTitle)
    private val playlistNumberOfTracks = itemView.findViewById<TextView>(R.id.numberOfTracks)

    fun bind(playlist: Playlist) {

        Glide.with(itemView.context)
            .load(playlist.imageUri)
            .placeholder(R.drawable.track_placeholder)
            .transform(CenterCrop(),  RoundedCorners(UtilFunctions().dpToPx(8f, itemView.context)))
            .into(playlistCover)

        playlistTitle.text = playlist.title

        playlistNumberOfTracks.text = itemView.context.resources.getQuantityString(R.plurals.tracks_plurals, playlist.numberOfTracks.toInt(), playlist.numberOfTracks.toInt())

        itemView.setOnClickListener {
            onClick.invoke(playlist.id!!)
        }
    }
}