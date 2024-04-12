package com.ivanpodlesnykh.playlistmaker.domain.media.models

sealed interface FavoriteTracksState {
    data object NoContent : FavoriteTracksState
}