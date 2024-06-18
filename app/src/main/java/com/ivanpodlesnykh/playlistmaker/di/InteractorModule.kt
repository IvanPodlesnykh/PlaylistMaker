package com.ivanpodlesnykh.playlistmaker.di

import com.ivanpodlesnykh.playlistmaker.domain.db.api.FavoriteTracksDatabaseInteractor
import com.ivanpodlesnykh.playlistmaker.domain.db.impl.FavoriteTracksDatabaseInteractorImpl
import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerInteractor
import com.ivanpodlesnykh.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchInteractor
import com.ivanpodlesnykh.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.ivanpodlesnykh.playlistmaker.domain.settings.api.SettingsInteractor
import com.ivanpodlesnykh.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.ivanpodlesnykh.playlistmaker.domain.sharing.api.SharingInteractor
import com.ivanpodlesnykh.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    factory<FavoriteTracksDatabaseInteractor> {
        FavoriteTracksDatabaseInteractorImpl(get())
    }

}