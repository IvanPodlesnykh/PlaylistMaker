package com.ivanpodlesnykh.playlistmaker.domain.settings.api

import com.ivanpodlesnykh.playlistmaker.domain.settings.models.ThemeSettings
import com.ivanpodlesnykh.playlistmaker.domain.sharing.models.EmailData

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)

    fun getShareAppLink(): String
    fun getTermsLink(): String
    fun getSupportEmailData(): EmailData
}