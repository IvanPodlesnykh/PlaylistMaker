package com.ivanpodlesnykh.playlistmaker.domain.search.impl

import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchInteractor
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchRepository
import com.ivanpodlesnykh.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {

    override fun searchTrack(
        request: String,
    ) : Flow<Pair<List<Track>?, String?>> {
        return searchRepository.searchTrack(request)
            .map {
                when (it) {
                    is Resource.Success -> Pair(it.data, "")
                    is Resource.Error -> Pair(null, it.message)
                }
            }
    }

    override suspend fun loadSearchHistory(): List<Track> {
        return searchRepository.loadSearchHistory()
    }

    override fun isSearchHistoryEmpty(): Boolean {
        return searchRepository.isSearchHistoryEmpty()
    }

    override fun clearSearchHistory() {
        searchRepository.clearSearchHistory()
    }
}