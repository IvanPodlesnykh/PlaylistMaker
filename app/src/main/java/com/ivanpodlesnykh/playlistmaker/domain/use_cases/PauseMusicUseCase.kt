package com.ivanpodlesnykh.playlistmaker.domain.use_cases

import com.ivanpodlesnykh.playlistmaker.domain.repository.PlayerRepository

class PauseMusicUseCase(
    private val playerRepository: PlayerRepository
) {
    fun execute() {
        playerRepository.pause()
    }
}