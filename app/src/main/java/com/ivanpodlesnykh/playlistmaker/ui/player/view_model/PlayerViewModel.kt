package com.ivanpodlesnykh.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanpodlesnykh.playlistmaker.domain.db.api.FavoriteTracksDatabaseInteractor
import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerInteractor
import com.ivanpodlesnykh.playlistmaker.domain.player.models.PlayerState
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val track: Track,
                      private val playerInteractor: PlayerInteractor,
                      private val favoriteTracksDatabaseInteractor: FavoriteTracksDatabaseInteractor) : ViewModel() {

    private var timerJob: Job? = null

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    private val currentPlaytimeLiveData = MutableLiveData<String>()
    fun getCurrentPlaytimeLiveData(): LiveData<String> = currentPlaytimeLiveData

    private val favoriteTrackLiveData = MutableLiveData(track.isFavorite)
    fun getFavoriteTrackLiveData(): LiveData<Boolean> = favoriteTrackLiveData

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerStateLiveData.value == PlayerState.STATE_PLAYING) {
                delay(UPDATE_TIME)
                if(playerStateLiveData.value == PlayerState.STATE_PLAYING)
                    currentPlaytimeLiveData.value = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(playerInteractor.getCurrentPlaytime())
            }
        }
    }
    init {
        playerInteractor.preparePlayer(track.previewUrl){
            playerStateLiveData.value = PlayerState.STATE_PREPARED
        }
    }

    fun play() {
        playerInteractor.playMusic()
        playerStateLiveData.value = playerInteractor.getPlayerState()
        startTimer()
    }

    fun pause() {
        playerInteractor.pauseMusic()
        playerStateLiveData.value = playerInteractor.getPlayerState()
    }

    override fun onCleared() {
        playerInteractor.stopPlayer()
        super.onCleared()
    }

    companion object {

        private const val UPDATE_TIME = 300L
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if(track.isFavorite) {
                track.isFavorite = false
                favoriteTracksDatabaseInteractor.deleteTrack(track.trackId)
                favoriteTrackLiveData.value = false
            }
            else {
                track.isFavorite = true
                favoriteTracksDatabaseInteractor.addTrackToFavorite(track)
                favoriteTrackLiveData.value = true
            }
        }
    }
}