package com.ivanpodlesnykh.playlistmaker.domain.use_cases

import com.ivanpodlesnykh.playlistmaker.domain.repository.PlayerRepository

class PreparePlayerUseCase(
    private val playerRepository: PlayerRepository
) {

    fun execute(url: String) {
        playerRepository.preparePlayer(url)
    }
}