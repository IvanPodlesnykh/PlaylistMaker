package com.ivanpodlesnykh.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import com.ivanpodlesnykh.playlistmaker.domain.settings.api.SettingsInteractor
import com.ivanpodlesnykh.playlistmaker.domain.settings.models.ThemeSettings
import com.ivanpodlesnykh.playlistmaker.domain.sharing.api.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    fun isNightMode(): Boolean {
        when(settingsInteractor.getThemeSettings()){
            ThemeSettings.NIGHT_MODE -> return true
            ThemeSettings.DAYLIGHT_MODE -> return false
        }
    }

    fun updateTheme(isNightMode: Boolean) {
        when(isNightMode){
            true -> settingsInteractor.updateThemeSettings(ThemeSettings.NIGHT_MODE)
            false -> settingsInteractor.updateThemeSettings(ThemeSettings.DAYLIGHT_MODE)
        }
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openUserAgreement() {
        sharingInteractor.openTerms()
    }

    fun contactUserSupport() {
        sharingInteractor.openSupport()
    }
}