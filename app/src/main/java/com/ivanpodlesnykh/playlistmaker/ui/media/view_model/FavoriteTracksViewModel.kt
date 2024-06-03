package com.ivanpodlesnykh.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanpodlesnykh.playlistmaker.domain.db.api.FavoriteTracksDatabaseInteractor
import com.ivanpodlesnykh.playlistmaker.domain.media.models.FavoriteTracksState
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteTracksDatabaseInteractor: FavoriteTracksDatabaseInteractor
) : ViewModel() {

    init {
        updateState()
    }

    private val stateLiveData = MutableLiveData<FavoriteTracksState>()
    fun observeState(): LiveData<FavoriteTracksState> = stateLiveData

    fun updateState() {
        viewModelScope.launch {
            favoriteTracksDatabaseInteractor.getAllFavoriteTracks().collect {
                if(it.isEmpty()) stateLiveData.value = FavoriteTracksState.NoContent
                else stateLiveData.value = FavoriteTracksState.ShowContent(it.reversed())
            }
        }
    }

}