package com.ivanpodlesnykh.playlistmaker.domain.media.models

data class Playlist(
    val id: Long? = null,
    val title: String,
    val description: String,
    val imageUri: String,
    val tracks: String,
    val numberOfTracks: Long
)
