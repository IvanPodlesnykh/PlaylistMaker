package com.ivanpodlesnykh.playlistmaker.domain.repository

interface PlayerRepository {

    fun play()

    fun pause()

    fun stop()

    fun preparePlayer(url: String)

    fun getState(): Int
}