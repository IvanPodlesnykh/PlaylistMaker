package com.ivanpodlesnykh.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanpodlesnykh.playlistmaker.domain.media.api.PlaylistInteractor
import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistId: Long,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val playlistLiveData = MutableLiveData<Playlist>()
    fun getPlaylistLiveData(): LiveData<Playlist> = playlistLiveData

    private val listOfTracks = MutableLiveData<List<Track>>()
    fun getListOfTracksLiveData(): LiveData<List<Track>> = listOfTracks

    init {
        getPlaylist()

        getTrackList()
    }

    fun getPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistLiveData.postValue(playlistInteractor.getPlaylist(playlistId))
        }
    }

    fun getTrackList() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getAllTracksFromPlaylist(playlistId).collect{
                listOfTracks.postValue(it)
            }
        }
    }

    fun deleteTrack(playlistId: Long, trackId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deleteTrack(playlistId, trackId)
            getTrackList()
        }
    }

    fun sharePlaylist() {
        viewModelScope.launch(Dispatchers.Default) {
            playlistInteractor.sharePlaylist(playlistId)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deletePlaylist(playlistId)
        }
    }

}