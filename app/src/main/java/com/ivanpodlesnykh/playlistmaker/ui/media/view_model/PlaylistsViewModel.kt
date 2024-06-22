package com.ivanpodlesnykh.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanpodlesnykh.playlistmaker.domain.media.api.PlaylistInteractor
import com.ivanpodlesnykh.playlistmaker.domain.media.models.PlaylistsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    init {
        getPlaylists()
    }

    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getAllPlaylists().collect{
                if(it.isEmpty()) stateLiveData.postValue(PlaylistsState.NoPlaylists)
                else stateLiveData.postValue(PlaylistsState.Content(it))
            }
        }
    }
}