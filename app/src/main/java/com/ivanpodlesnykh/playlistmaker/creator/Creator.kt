package com.ivanpodlesnykh.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.ivanpodlesnykh.playlistmaker.data.player.PlayerRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.data.search.SearchRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerInteractor
import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerRepository
import com.ivanpodlesnykh.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchInteractor
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchRepository
import com.ivanpodlesnykh.playlistmaker.domain.search.impl.SearchInteractorImpl

object Creator {
    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun getPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    private fun getSearchRepository(application: Application): SearchRepository {
        return SearchRepositoryImpl(application)
    }

    fun getSearchInteractor(application: Application): SearchInteractor {
        return SearchInteractorImpl(getSearchRepository(application))
    }
}