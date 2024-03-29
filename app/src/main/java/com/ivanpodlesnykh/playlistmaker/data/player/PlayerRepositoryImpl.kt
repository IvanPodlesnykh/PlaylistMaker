package com.ivanpodlesnykh.playlistmaker.data.player

import android.media.MediaPlayer
import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerRepository
import com.ivanpodlesnykh.playlistmaker.domain.player.models.PlayerState

class PlayerRepositoryImpl(
    private val player: MediaPlayer
): PlayerRepository {


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