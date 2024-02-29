package com.ivanpodlesnykh.playlistmaker.data

import android.media.MediaPlayer
import com.ivanpodlesnykh.playlistmaker.domain.api.PlayerRepository
import com.ivanpodlesnykh.playlistmaker.domain.models.PlayerState

class PlayerRepositoryImpl: PlayerRepository {

    private lateinit var player: MediaPlayer

    private var state = PlayerState.STATE_DEFAULT
    override fun play() {
        player.start()
        state = PlayerState.STATE_PLAYING
    }

    override fun pause() {
        player.pause()
        state = PlayerState.STATE_PAUSED
    }

    override fun stop() {
        player.release()
    }

    override fun preparePlayer(url: String, onCompletionListener: () -> Unit) {
        player = MediaPlayer()

        player.setDataSource(url)
        player.prepareAsync()

        player.setOnPreparedListener {
            state = PlayerState.STATE_DEFAULT
        }

        player.setOnCompletionListener {
            state = PlayerState.STATE_PREPARED
            onCompletionListener.invoke()
        }
    }

    override fun getState(): PlayerState {
        return state
    }

    override fun getCurrentPlaytimeMillis(): Int {
        return player.currentPosition
    }

}