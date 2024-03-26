package com.ivanpodlesnykh.playlistmaker.domain.player.impl

import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerInteractor
import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerRepository
import com.ivanpodlesnykh.playlistmaker.domain.player.models.PlayerState

class PlayerInteractorImpl(
    private val playerRepository: PlayerRepository
): PlayerInteractor {
    override fun playMusic() {
        playerRepository.play()
    }

    override fun pauseMusic() {
        playerRepository.pause()
    }

    override fun preparePlayer(url: String, onCompletionListener: () -> Unit) {
        playerRepository.preparePlayer(url, onCompletionListener)
    }

    override fun stopPlayer() {
        playerRepository.stop()
    }

    override fun getPlayerState(): PlayerState {
        return playerRepository.getState()
    }

    override fun getCurrentPlaytime(): Int {
        return playerRepository.getCurrentPlaytimeMillis()
    }


}