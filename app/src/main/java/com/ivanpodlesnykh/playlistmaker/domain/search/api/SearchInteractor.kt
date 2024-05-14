package com.ivanpodlesnykh.playlistmaker.domain.search.api

import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun searchTrack(request: String): Flow<Pair<List<Track>?, String?>>

    fun loadSearchHistory() : List<Track>

    fun clearSearchHistory()
}