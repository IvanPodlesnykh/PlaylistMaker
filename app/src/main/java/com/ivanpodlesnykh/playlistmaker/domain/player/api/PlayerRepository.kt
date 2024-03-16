package com.ivanpodlesnykh.playlistmaker.domain.player.api

import com.ivanpodlesnykh.playlistmaker.domain.player.models.PlayerState

interface PlayerRepository {

    fun play()

    fun pause()

    fun stop()

    fun preparePlayer(url: String, onCompletionListener: () -> Unit)

    fun getState(): PlayerState

    fun getCurrentPlaytimeMillis(): Int
}