package com.ivanpodlesnykh.playlistmaker.ui.search.view_model

import android.app.Application
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ivanpodlesnykh.playlistmaker.creator.Creator
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchInteractor
import com.ivanpodlesnykh.playlistmaker.domain.search.models.ErrorType
import com.ivanpodlesnykh.playlistmaker.domain.search.models.SearchState

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private val searchStateLiveData = MutableLiveData<SearchState>(SearchState.Default)
    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    private var latestSearchText: String? = null
    fun searchDebounce(changedText: String){
        if (changedText.isEmpty()) {
            mainThreadHandler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        }
        if (latestSearchText == changedText)
            return
        latestSearchText = changedText
        mainThreadHandler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable {
            searchRequest(changedText)
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mainThreadHandler.postDelayed(searchRunnable, SEARCH_REQUEST_TOKEN, SEARCH_DEBOUNCE_DELAY)
        } else {
            val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
            mainThreadHandler.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN, postTime)
        }
    }

    fun searchRequest(newSearchRequest: String) {
        if(newSearchRequest.isNotEmpty()){
            renderState(SearchState.Loading)
            searchInteractor.searchTrack(newSearchRequest, object : SearchInteractor.SearchConsumer{
                override fun consume(foundTrackList: List<Track>, message: String?) {
                    if(message == "no_connection") {
                        renderState(
                            SearchState.Error(ErrorType.NO_CONNECTION)
                        )
                        return
                    }
                    if (foundTrackList.isNotEmpty()) {
                        renderState(
                            SearchState.ShowTrackList(
                                foundTrackList
                            )
                        )
                        return
                    } else {
                        renderState(
                            SearchState.Error(
                                ErrorType.NOT_FOUND
                            )
                        )
                        return
                    }
                }

            })
        }
    }

    fun renderState(state: SearchState){
        searchStateLiveData.postValue(state)
    }

    fun getSearchHistory() {
        renderState(SearchState.ShowHistory(searchInteractor.loadSearchHistory()))
    }

    fun isSearchHistoryEmpty() : Boolean {
        return searchInteractor.loadSearchHistory().isEmpty()
    }

    fun clearSearchHistory() {
        renderState(SearchState.Default)
        searchInteractor.clearSearchHistory()
    }

    companion object {

        private val SEARCH_REQUEST_TOKEN = Any()

        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun getViewModelFactory(application: Application): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val searchInteractor = Creator.getSearchInteractor(application)

                SearchViewModel(searchInteractor)
            }
        }
    }
}