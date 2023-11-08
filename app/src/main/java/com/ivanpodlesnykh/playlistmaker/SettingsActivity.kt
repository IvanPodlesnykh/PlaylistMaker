package com.ivanpodlesnykh.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.settings_back_button)
        backButton.setOnClickListener{
//            val mainActivityIntent = Intent(this@SettingsActivity, MainActivity::class.java)
//            startActivity(mainActivityIntent)
            this.finish()
        }

        val shareButton = findViewById<ImageView>(R.id.share_app_button)
        shareButton.setOnClickListener{

        }

        val supportButton = findViewById<ImageView>(R.id.support_button)
        supportButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.dev_mail)))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_to_dev_topic))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_to_dev_text))
            startActivity(intent)
        }

        val userAgreementButton = findViewById<ImageView>(R.id.user_agreement_button)
        userAgreementButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_agreement_url)))
            startActivity(intent)
        }
    }
}