package com.ivanpodlesnykh.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchInteractor
import com.ivanpodlesnykh.playlistmaker.domain.search.models.ErrorType
import com.ivanpodlesnykh.playlistmaker.domain.search.models.SearchState
import com.ivanpodlesnykh.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val searchStateLiveData = MutableLiveData<SearchState>(SearchState.Default)
    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    val delaySearch = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
        searchText -> searchRequest(searchText)
    }

    fun searchRequest(newSearchRequest: String) {
        if(newSearchRequest.isNotEmpty()){
            renderState(SearchState.Loading)
            viewModelScope.launch {
                searchInteractor.searchTrack(newSearchRequest)
                    .collect{
                        if(it.second == "no_connection") {
                            renderState(
                                SearchState.Error(ErrorType.NO_CONNECTION)
                            )
                            return@collect
                        }

                        if(it.first == null) {
                            renderState(
                                SearchState.Error(ErrorType.NO_CONNECTION)
                            )
                            return@collect
                        } else {
                            if (it.first!!.isNotEmpty()) {
                                renderState(SearchState.ShowTrackList(it.first!!))
                            }
                            else renderState(SearchState.Error(ErrorType.NOT_FOUND))
                        }
                    }
            }
        }
    }

    fun renderState(state: SearchState){
        searchStateLiveData.postValue(state)
    }

    fun getSearchHistory() {
        renderState(SearchState.ShowHistory(searchInteractor.loadSearchHistory()))
    }

    fun updateSearchHistory() {
        renderState(SearchState.UpdateHistory(searchInteractor.loadSearchHistory()))
    }

    fun isSearchHistoryEmpty() : Boolean {
        return searchInteractor.loadSearchHistory().isEmpty()
    }

    fun clearSearchHistory() {
        renderState(SearchState.Default)
        searchInteractor.clearSearchHistory()
    }

    companion object {

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}