package com.ivanpodlesnykh.playlistmaker.ui

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.ivanpodlesnykh.playlistmaker.di.dataModule
import com.ivanpodlesnykh.playlistmaker.di.interactorModule
import com.ivanpodlesnykh.playlistmaker.di.repositoryModule
import com.ivanpodlesnykh.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        loadTheme()
    }

    private fun loadTheme() {
        val defaultTheme = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        val sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE)
        if(sharedPreferences.getBoolean("DARK_THEME_KEY", defaultTheme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}