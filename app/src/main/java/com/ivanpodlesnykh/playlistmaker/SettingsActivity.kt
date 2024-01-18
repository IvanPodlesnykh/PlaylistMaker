package com.ivanpodlesnykh.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        handleBackButton()

        handleSwitch()

        handleShareButton()

        handleSupportButton()

        handleUserAgreementButton()
    }

    private fun handleBackButton() {
        val backButton = findViewById<ImageView>(R.id.settings_back_button)
        backButton.setOnClickListener{
            this.finish()
        }
    }

    private fun handleSwitch() {
        val darkThemeSwitch = findViewById<Switch>(R.id.theme_switch)
        if(loadTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            darkThemeSwitch.toggle()
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        darkThemeSwitch.setOnCheckedChangeListener{ switcher, isChecked ->
            if(isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveTheme(true)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                saveTheme(false)
            }
        }
    }

    private fun saveTheme(isDark: Boolean) {
        val sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply{
            putBoolean("DARK_THEME_KEY", isDark)
        }.apply()
    }

    private fun loadTheme(): Boolean {
        val defaultTheme = resources.configuration.uiMode == Configuration.UI_MODE_NIGHT_YES
        val sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE)
        return sharedPreferences.getBoolean("DARK_THEME_KEY", defaultTheme)
    }

    private fun handleShareButton() {
        val shareButton = findViewById<ImageView>(R.id.share_app_button)
        shareButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.practicum_link))
            intent.type = "text/plain"
            startActivity(intent)
        }
    }

    private fun handleSupportButton() {
        val supportButton = findViewById<ImageView>(R.id.support_button)
        supportButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).apply {
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.dev_mail)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_to_dev_topic))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_to_dev_text))
            }
            startActivity(intent)
        }
    }

    private fun handleUserAgreementButton() {
        val userAgreementButton = findViewById<ImageView>(R.id.user_agreement_button)
        userAgreementButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_agreement_url)))
            startActivity(intent)
        }
    }
}

