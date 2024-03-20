package com.ivanpodlesnykh.playlistmaker.domain.search.impl

import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchInteractor
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchRepository
import com.ivanpodlesnykh.playlistmaker.util.Resource
import java.util.concurrent.Executors

class SearchInteractorImpl(private val seachRepository: SearchRepository) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTrack(
        request: String,
        consumer: SearchInteractor.SearchConsumer
    ) {
        executor.execute {
            when(val response = seachRepository.searchTrack(request)){
                is Resource.Error -> {
                    consumer.consume(emptyList(), response.message)
                }
                is Resource.Success -> consumer.consume(response.data!!, response.message)
            }
        }
    }

    override fun loadSearchHistory(): List<Track> {
        return seachRepository.loadSearchHistory()
    }

    override fun clearSearchHistory() {
        seachRepository.clearSearchHistory()
    }
}