package com.ivanpodlesnykh.playlistmaker.creator

import com.ivanpodlesnykh.playlistmaker.data.player.PlayerRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerInteractor
import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerRepository
import com.ivanpodlesnykh.playlistmaker.domain.player.impl.PlayerInteractorImpl

object Creator {
    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun getPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }
}