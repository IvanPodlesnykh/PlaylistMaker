package com.ivanpodlesnykh.playlistmaker.data.settings

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.ivanpodlesnykh.playlistmaker.domain.settings.api.SettingsRepository
import com.ivanpodlesnykh.playlistmaker.domain.settings.models.ThemeSettings

class SettingsRepositoryImpl(private val application: Application) : SettingsRepository {

    private val sharedPreferences = application.getSharedPreferences("sharedPref", Application.MODE_PRIVATE)
    override fun getThemeSettings(): ThemeSettings {
        val defaultTheme = (application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        if(sharedPreferences.getBoolean("DARK_THEME_KEY", defaultTheme)) {
            return ThemeSettings.NIGHT_MODE
        }
        else {
            return ThemeSettings.DAYLIGHT_MODE
        }
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        val editor = sharedPreferences.edit()
        when (settings) {
            ThemeSettings.NIGHT_MODE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.apply {
                    putBoolean("DARK_THEME_KEY", true)
                }.apply()
            }
            ThemeSettings.DAYLIGHT_MODE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.apply {
                    putBoolean("DARK_THEME_KEY", false)
                }.apply()
            }
        }
    }
}