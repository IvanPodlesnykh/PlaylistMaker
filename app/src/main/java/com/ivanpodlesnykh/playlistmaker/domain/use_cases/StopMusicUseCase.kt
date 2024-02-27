package com.ivanpodlesnykh.playlistmaker.domain.use_cases

import com.ivanpodlesnykh.playlistmaker.domain.repository.PlayerRepository

class StopMusicUseCase(
    private val playerRepository: PlayerRepository
) {
    fun execute() {
        playerRepository.stop()
    }
}