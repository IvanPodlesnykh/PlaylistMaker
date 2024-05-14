package com.ivanpodlesnykh.playlistmaker.data.search

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.ivanpodlesnykh.playlistmaker.data.search.network.MusicApi
import com.ivanpodlesnykh.playlistmaker.data.search.shared_preferences.SearchHistory
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchRepository
import com.ivanpodlesnykh.playlistmaker.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class SearchRepositoryImpl(
    private val application: Application,
    private val musicService: MusicApi,
    private val searchHistory: SearchHistory): SearchRepository {

    override fun searchTrack(request: String): Flow<Resource<List<Track>>> = flow {
        emit(doRequest(request))
    }

    private suspend fun doRequest(request: String): Resource<List<Track>> {
        if(!isConnected()) {
            return Resource.Error("no_connection")
        }

        return withContext(Dispatchers.IO) {
            val response = musicService.search(request)

            if(response.resultCount != 0) {
                Resource.Success(response.results)
            } else {
                Resource.Success(emptyList())
            }
        }
    }

    override fun loadSearchHistory(): List<Track> {
        return searchHistory.getTrackList()
    }

    override fun clearSearchHistory() {
        searchHistory.clearTrackList()
    }

    private fun isConnected(): Boolean {
        val connectivityManager = application.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}