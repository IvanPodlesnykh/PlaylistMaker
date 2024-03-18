package com.ivanpodlesnykh.playlistmaker.domain.sharing.impl

import com.ivanpodlesnykh.playlistmaker.domain.sharing.api.ExternalNavigator
import com.ivanpodlesnykh.playlistmaker.domain.sharing.api.SharingInteractor
import com.ivanpodlesnykh.playlistmaker.domain.sharing.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return "https://practicum.yandex.ru/profile/android-developer/"
    }

    private fun getTermsLink(): String {
        return "https://yandex.ru/legal/practicum_offer/"
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(arrayOf("ivanapodlesnykh@yandex.ru"),
            "Сообщение разработчикам и разработчицам приложения Playlist Maker",
            "Спасибо разработчикам и разработчицам за крутое приложение!")
    }

}