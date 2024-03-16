package com.ivanpodlesnykh.playlistmaker.data.search

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import com.ivanpodlesnykh.playlistmaker.data.search.network.MusicApi
import com.ivanpodlesnykh.playlistmaker.data.search.shared_preferences.SearchHistory
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchRepository
import com.ivanpodlesnykh.playlistmaker.util.Resource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchRepositoryImpl(private val application: Application): SearchRepository {
    private val baseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val musicService = retrofit.create(MusicApi::class.java)

    private val searchHistory = SearchHistory(application.getSharedPreferences("sharedPref",
        AppCompatActivity.MODE_PRIVATE
    ))

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