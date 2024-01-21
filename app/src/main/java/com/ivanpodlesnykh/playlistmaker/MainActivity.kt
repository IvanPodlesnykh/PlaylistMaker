package com.ivanpodlesnykh.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handleButtons()
    }

    private fun handleButtons() {
        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaButton = findViewById<Button>(R.id.media_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        val searchButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }

        searchButton.setOnClickListener(searchButtonClickListener)

        mediaButton.setOnClickListener{
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        settingsButton.setOnClickListener{
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}