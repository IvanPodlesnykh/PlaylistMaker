package com.ivanpodlesnykh.playlistmaker.data.search

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.ivanpodlesnykh.playlistmaker.data.converters.FavoriteTracksDbConverter
import com.ivanpodlesnykh.playlistmaker.data.db.FavoriteTracksDatabase
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
    private val searchHistory: SearchHistory,
    private val database: FavoriteTracksDatabase,
    private val favoriteTracksDbConverter: FavoriteTracksDbConverter): SearchRepository {

    override fun searchTrack(request: String): Flow<Resource<List<Track>>> = flow {
        emit(doRequest(request))
    }

    private suspend fun doRequest(request: String): Resource<List<Track>> {
        if(!isConnected()) {
            return Resource.Error("no_connection")
        }

        return withContext(Dispatchers.IO) {

            val favoriteTracksList = database.getFavoriteTracksDao().getAllTracks()

            try {
                val response = musicService.search(request)

                if(response.resultCount != 0) {
                    response.results.map {
                        if (favoriteTracksList.contains(favoriteTracksDbConverter.map(it)))
                            it.isFavorite = true
                    }
                    Resource.Success(response.results)
                } else {
                    Resource.Success(emptyList())
                }
            } catch (e: Throwable) {
                Resource.Error("no_connection")
            }

        }
    }

    override suspend fun loadSearchHistory(): List<Track> {
        return withContext(Dispatchers.IO) {
            val favoriteTracksList = database.getFavoriteTracksDao().getAllTracks()

            searchHistory.getTrackList().map {
                it.isFavorite = favoriteTracksList.contains(favoriteTracksDbConverter.map(it))
                it
            }
        }
    }

    override fun isSearchHistoryEmpty(): Boolean {
        return searchHistory.getTrackList().isEmpty()
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