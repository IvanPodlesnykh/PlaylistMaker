package com.ivanpodlesnykh.playlistmaker.domain.media.models

sealed interface PlaylistsState {
    data object NoPlaylists: PlaylistsState
}