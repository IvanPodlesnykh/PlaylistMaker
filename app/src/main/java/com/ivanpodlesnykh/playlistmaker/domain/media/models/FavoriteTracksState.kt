package com.ivanpodlesnykh.playlistmaker.domain.media.models

import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track

sealed interface FavoriteTracksState {
    data object NoContent : FavoriteTracksState

    data class ShowContent(val listOfFavoriteTracks: List<Track>) : FavoriteTracksState
}