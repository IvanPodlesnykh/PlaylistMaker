package com.ivanpodlesnykh.playlistmaker.creator

import android.app.Application
import com.ivanpodlesnykh.playlistmaker.data.player.PlayerRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.data.search.SearchRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.data.settings.SettingsRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerInteractor
import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerRepository
import com.ivanpodlesnykh.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchInteractor
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchRepository
import com.ivanpodlesnykh.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.ivanpodlesnykh.playlistmaker.domain.settings.api.SettingsInteractor
import com.ivanpodlesnykh.playlistmaker.domain.settings.api.SettingsRepository
import com.ivanpodlesnykh.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.ivanpodlesnykh.playlistmaker.domain.sharing.api.ExternalNavigator
import com.ivanpodlesnykh.playlistmaker.domain.sharing.api.SharingInteractor
import com.ivanpodlesnykh.playlistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {

    private lateinit var application: Application

    fun setApplication(application: Application) {
        this.application = application
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun getPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    private fun getSearchRepository(application: Application): SearchRepository {
        return SearchRepositoryImpl(application)
    }

    fun getSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(getSearchRepository(application))
    }

    private fun getExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }

    fun getSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator())
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(application)
    }

    fun  getSettingsInteractor() : SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }
}