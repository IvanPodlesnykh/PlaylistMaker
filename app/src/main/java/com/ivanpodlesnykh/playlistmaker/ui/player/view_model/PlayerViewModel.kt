package com.ivanpodlesnykh.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerInteractor
import com.ivanpodlesnykh.playlistmaker.domain.player.models.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(url: String, private val playerInteractor: PlayerInteractor) : ViewModel() {

    private var timerJob: Job? = null

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    private val currentPlaytimeLiveData = MutableLiveData<String>()
    fun getCurrentPlaytimeLiveData(): LiveData<String> = currentPlaytimeLiveData

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerStateLiveData.value == PlayerState.STATE_PLAYING) {
                delay(UPDATE_TIME)
                currentPlaytimeLiveData.value = SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(playerInteractor.getCurrentPlaytime())
            }
        }
    }
    init {
        playerInteractor.preparePlayer(url){
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

        private const val UPDATE_TIME = 200L
    }
}