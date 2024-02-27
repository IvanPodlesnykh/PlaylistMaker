package com.ivanpodlesnykh.playlistmaker.domain.api

import com.ivanpodlesnykh.playlistmaker.domain.models.PlayerState

interface PlayerRepository {

    fun play()

    fun pause()

    fun stop()

    fun preparePlayer(url: String, onCompletionListener: () -> Unit)

    fun getState(): PlayerState

    fun getCurrentPlaytimeMillis(): Int
}