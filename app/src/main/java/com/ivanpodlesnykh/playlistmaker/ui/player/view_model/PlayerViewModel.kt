package com.ivanpodlesnykh.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ivanpodlesnykh.playlistmaker.creator.Creator
import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerInteractor
import com.ivanpodlesnykh.playlistmaker.domain.player.models.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(url: String, private val playerInteractor: PlayerInteractor) : ViewModel() {

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private var playerStateLiveData = MutableLiveData<PlayerState>()
    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    private val currentPlaytimeLiveData = MutableLiveData<String>()
    fun getCurrentPlaytimeLiveData(): LiveData<String> = currentPlaytimeLiveData

    private val updateTimeObject = object : Runnable {
        override fun run() {
            mainThreadHandler.post {
                currentPlaytimeLiveData.value = SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(playerInteractor.getCurrentPlaytime())
                mainThreadHandler?.postDelayed(this, UPDATE_TIME)
            }
        }
    }
    init {
        playerInteractor.preparePlayer(url){
            playerStateLiveData.value = PlayerState.STATE_PREPARED
            mainThreadHandler.removeCallbacks(updateTimeObject)
        }
    }

    fun play() {
        updateTime()
        playerInteractor.playMusic()
        playerStateLiveData.value = playerInteractor.getPlayerState()
    }

    fun pause() {
        mainThreadHandler.removeCallbacks(updateTimeObject)
        playerInteractor.pauseMusic()
        playerStateLiveData.value = playerInteractor.getPlayerState()
    }

    private fun updateTime() {
        mainThreadHandler.post(updateTimeObject)
    }

    override fun onCleared() {
        playerInteractor.stopPlayer()
        mainThreadHandler.removeCallbacks(updateTimeObject)
        super.onCleared()
    }

    companion object {

        private const val UPDATE_TIME = 200L
        fun getViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val playerInteractor = Creator.getPlayerInteractor()

                PlayerViewModel(url, playerInteractor)
            }
        }
    }
}