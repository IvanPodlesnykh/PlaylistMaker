package com.ivanpodlesnykh.playlistmaker.domain.sharing.api

import com.ivanpodlesnykh.playlistmaker.domain.sharing.models.EmailData

interface ExternalNavigator {
    fun shareLink(url: String)
    fun openLink(url: String)
    fun openEmail(emailData: EmailData)
}