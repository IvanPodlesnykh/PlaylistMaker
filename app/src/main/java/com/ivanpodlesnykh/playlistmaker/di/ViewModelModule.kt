package com.ivanpodlesnykh.playlistmaker.di

import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.ui.media.view_model.CreatePlaylistViewModel
import com.ivanpodlesnykh.playlistmaker.ui.media.view_model.FavoriteTracksViewModel
import com.ivanpodlesnykh.playlistmaker.ui.media.view_model.PlaylistsViewModel
import com.ivanpodlesnykh.playlistmaker.ui.player.view_model.PlayerViewModel
import com.ivanpodlesnykh.playlistmaker.ui.search.view_model.SearchViewModel
import com.ivanpodlesnykh.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.dsl.module

val viewModelModule = module {

    factory {
        SettingsViewModel(get(), get())
    }

    factory {
        SearchViewModel(get())
    }

    factory {(track: Track) ->
        PlayerViewModel(track, get(), get(), get())
    }

    factory {
        FavoriteTracksViewModel(get())
    }

    factory {
        PlaylistsViewModel(get())
    }

    factory {
        CreatePlaylistViewModel(get())
    }

}