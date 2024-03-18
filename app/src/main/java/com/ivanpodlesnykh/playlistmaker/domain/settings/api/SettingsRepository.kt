package com.ivanpodlesnykh.playlistmaker.domain.settings.api

import com.ivanpodlesnykh.playlistmaker.domain.settings.models.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}