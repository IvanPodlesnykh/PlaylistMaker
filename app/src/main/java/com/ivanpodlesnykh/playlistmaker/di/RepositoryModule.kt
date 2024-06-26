package com.ivanpodlesnykh.playlistmaker.di

import com.ivanpodlesnykh.playlistmaker.data.db.impl.FavoriteTracksDatabaseRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.data.media.impl.PlaylistRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.data.player.PlayerRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.data.search.SearchRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.data.settings.SettingsRepositoryImpl
import com.ivanpodlesnykh.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.ivanpodlesnykh.playlistmaker.domain.db.api.FavoriteTracksDatabaseRepository
import com.ivanpodlesnykh.playlistmaker.domain.media.api.PlaylistRepository
import com.ivanpodlesnykh.playlistmaker.domain.player.api.PlayerRepository
import com.ivanpodlesnykh.playlistmaker.domain.search.api.SearchRepository
import com.ivanpodlesnykh.playlistmaker.domain.settings.api.SettingsRepository
import com.ivanpodlesnykh.playlistmaker.domain.sharing.api.ExternalNavigator
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    factory<SettingsRepository> {
        SettingsRepositoryImpl(androidApplication(), get())
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(androidApplication())
    }

    factory<SearchRepository> {
        SearchRepositoryImpl(androidApplication(), get(), get(), get(), get())
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    factory<FavoriteTracksDatabaseRepository> {
        FavoriteTracksDatabaseRepositoryImpl(get(), get(), get())
    }

    factory<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), androidContext(), get())
    }
}