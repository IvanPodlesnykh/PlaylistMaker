package com.ivanpodlesnykh.playlistmaker.domain.player.models

sealed class AddTrackToPlaylistState {

    data object Default: AddTrackToPlaylistState()

    data class TrackAdded(val playlistTitle: String): AddTrackToPlaylistState()

    data class TrackIsNotAdded(val playlistTitle: String): AddTrackToPlaylistState()

}