package com.ivanpodlesnykh.playlistmaker.di

import com.ivanpodlesnykh.playlistmaker.ui.main.view_model.MainViewModel
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

    factory {
        MainViewModel(get())
    }

    factory {(url: String) ->
        PlayerViewModel(url, get())
    }

}