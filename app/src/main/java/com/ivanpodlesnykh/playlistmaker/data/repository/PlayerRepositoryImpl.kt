package com.ivanpodlesnykh.playlistmaker.data.repository

import android.media.MediaPlayer
import com.ivanpodlesnykh.playlistmaker.domain.repository.PlayerRepository

class PlayerRepositoryImpl: PlayerRepository {

    lateinit var player: MediaPlayer

    private var state = STATE_DEFAULT
    override fun play() {
        player.start()
        state = STATE_PLAYING
    }

    override fun pause() {
        player.pause()
        state = STATE_PAUSED
    }

    override fun stop() {
        player.release()
    }

    override fun preparePlayer(url: String) {
        player.setDataSource(url)
        player.prepareAsync()

        player.setOnPreparedListener {
            state = STATE_DEFAULT
        }

        player.setOnCompletionListener {
            state = STATE_PREPARED
        }
    }

    override fun getState(): Int {
        return state
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}