package com.ivanpodlesnykh.playlistmaker.domain.api

import com.ivanpodlesnykh.playlistmaker.domain.models.PlayerState

interface PlayerInteractor {
    fun playMusic()

    fun pauseMusic()

    fun preparePlayer(url: String, onCompletionListener: () -> Unit)

    fun stopPlayer()

    fun getPlayerState(): PlayerState

    fun getCurrentPlaytime(): Int
}