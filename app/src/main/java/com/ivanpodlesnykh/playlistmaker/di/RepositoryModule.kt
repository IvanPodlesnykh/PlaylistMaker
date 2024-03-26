package com.ivanpodlesnykh.playlistmaker.di

import com.ivanpodlesnykh.playlistmaker.data.player.PlayerRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.data.search.SearchRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.data.settings.SettingsRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerRepository
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchRepository
import com.ivanpodlesnykh.playlistmaker.domain.settings.api.SettingsRepository
import com.ivanpodlesnykh.playlistmaker.domain.sharing.api.ExternalNavigator
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repositoryModule = module {

    factory<SettingsRepository> {
        SettingsRepositoryImpl(androidApplication(), get())
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(androidApplication())
    }

    factory<SearchRepository> {
        SearchRepositoryImpl(androidApplication(), get(), get())
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }
}