package com.ivanpodlesnykh.playlistmaker.data.settings

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.domain.settings.api.SettingsRepository
import com.ivanpodlesnykh.playlistmaker.domain.settings.models.ThemeSettings
import com.ivanpodlesnykh.playlistmaker.domain.sharing.models.EmailData

class SettingsRepositoryImpl(private val application: Application) : SettingsRepository {

    private val sharedPreferences =
        application.getSharedPreferences("sharedPref", Application.MODE_PRIVATE)

    override fun getThemeSettings(): ThemeSettings {
        val defaultTheme =
            (application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        if (sharedPreferences.getBoolean("DARK_THEME_KEY", defaultTheme)) {
            return ThemeSettings.NIGHT_MODE
        } else {
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

    override fun getShareAppLink(): String {
        return application.getString(R.string.practicum_link)
    }

    override fun getTermsLink(): String {
        return application.getString(R.string.user_agreement_url)
    }

    override fun getSupportEmailData(): EmailData {
        return EmailData(
            arrayOf(application.getString(R.string.dev_mail)),
            application.getString(R.string.mail_to_dev_topic),
            application.getString(R.string.mail_to_dev_text)
        )
    }
}