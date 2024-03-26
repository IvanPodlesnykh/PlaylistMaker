package com.ivanpodlesnykh.playlistmaker.data.search

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.ivanpodlesnykh.playlistmaker.data.search.network.MusicApi
import com.ivanpodlesnykh.playlistmaker.data.search.shared_preferences.SearchHistory
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchRepository
import com.ivanpodlesnykh.playlistmaker.util.Resource

class SearchRepositoryImpl(
    private val application: Application,
    private val musicService: MusicApi,
    private val searchHistory: SearchHistory): SearchRepository {

    override fun searchTrack(request: String): Resource<List<Track>> {

        if(!isConnected()) {
            return Resource.Error("no_connection")
        }

        val response = musicService.search(request).execute()

        if(response.code() == 200) {
            return Resource.Success(response.body()?.results?: emptyList())
        } else {
            return Resource.Error(response.code().toString())
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