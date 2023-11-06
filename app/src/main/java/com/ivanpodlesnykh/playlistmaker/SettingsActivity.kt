package com.ivanpodlesnykh.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
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
    }
}