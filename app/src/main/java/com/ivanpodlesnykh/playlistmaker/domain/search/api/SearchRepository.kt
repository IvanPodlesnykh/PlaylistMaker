package com.ivanpodlesnykh.playlistmaker.domain.search.api

import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchTrack(request: String) : Flow<Resource<List<Track>>>

    suspend fun loadSearchHistory() : List<Track>

    fun isSearchHistoryEmpty(): Boolean

    fun clearSearchHistory()
}