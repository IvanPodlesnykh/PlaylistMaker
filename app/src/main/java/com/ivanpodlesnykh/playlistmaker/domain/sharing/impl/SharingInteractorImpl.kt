package com.ivanpodlesnykh.playlistmaker.domain.sharing.impl

import com.ivanpodlesnykh.playlistmaker.domain.settings.api.SettingsRepository
import com.ivanpodlesnykh.playlistmaker.domain.sharing.api.ExternalNavigator
import com.ivanpodlesnykh.playlistmaker.domain.sharing.api.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val settingsRepository: SettingsRepository
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(settingsRepository.getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(settingsRepository.getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(settingsRepository.getSupportEmailData())
    }
}