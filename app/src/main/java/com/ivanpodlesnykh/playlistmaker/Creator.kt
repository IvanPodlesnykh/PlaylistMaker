package com.ivanpodlesnykh.playlistmaker

import com.ivanpodlesnykh.playlistmaker.data.PlayerRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.domain.api.PlayerInteractor
import com.ivanpodlesnykh.playlistmaker.domain.api.PlayerRepository
import com.ivanpodlesnykh.playlistmaker.domain.impl.PlayerInteractorImpl

object Creator {
    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun getPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }
}