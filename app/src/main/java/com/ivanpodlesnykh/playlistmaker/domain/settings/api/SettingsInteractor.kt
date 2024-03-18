package com.ivanpodlesnykh.playlistmaker.domain.settings.api

import com.ivanpodlesnykh.playlistmaker.domain.settings.models.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSettings(settings: ThemeSettings)
}