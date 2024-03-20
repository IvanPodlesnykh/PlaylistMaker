package com.ivanpodlesnykh.playlistmaker.domain.search.api

import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track

interface SearchInteractor {

    fun searchTrack(request: String, consumer: SearchConsumer)

    fun loadSearchHistory() : List<Track>

    fun clearSearchHistory()

    interface SearchConsumer {
        fun consume(foundTrackList: List<Track>, message: String?)
    }
}