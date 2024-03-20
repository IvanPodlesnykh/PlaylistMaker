package com.ivanpodlesnykh.playlistmaker.data.sharing

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.ivanpodlesnykh.playlistmaker.domain.sharing.api.ExternalNavigator
import com.ivanpodlesnykh.playlistmaker.domain.sharing.models.EmailData

class ExternalNavigatorImpl(private val application: Application) : ExternalNavigator {
    override fun shareLink(url: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, url)
        intent.type = "text/plain"
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    override fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    override fun openEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).apply {
            putExtra(Intent.EXTRA_EMAIL, emailData.emailAddresses)
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }
}