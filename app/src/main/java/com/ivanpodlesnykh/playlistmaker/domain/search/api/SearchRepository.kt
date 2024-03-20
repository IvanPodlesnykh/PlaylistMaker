package com.ivanpodlesnykh.playlistmaker.domain.search.api

import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.util.Resource

interface SearchRepository {

    fun searchTrack(request: String) : Resource<List<Track>>

    fun loadSearchHistory() : List<Track>

    fun clearSearchHistory()
}