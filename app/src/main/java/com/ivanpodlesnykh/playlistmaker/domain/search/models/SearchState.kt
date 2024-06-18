package com.ivanpodlesnykh.playlistmaker.domain.search.models

import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track

sealed class SearchState {

    data object Default: SearchState()

    data object Loading: SearchState()

    data class Error(val errorType: ErrorType) : SearchState()

    data class ShowHistory(val trackList: List<Track>) : SearchState()

    data class ShowTrackList(val trackList: List<Track>) : SearchState()
}