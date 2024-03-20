package com.ivanpodlesnykh.playlistmaker.domain.player.api

import com.ivanpodlesnykh.playlistmaker.domain.player.models.PlayerState

interface PlayerInteractor {
    fun playMusic()

    fun pauseMusic()

    fun preparePlayer(url: String, onCompletionListener: () -> Unit)

    fun stopPlayer()

    fun getPlayerState(): PlayerState

    fun getCurrentPlaytime(): Int
}