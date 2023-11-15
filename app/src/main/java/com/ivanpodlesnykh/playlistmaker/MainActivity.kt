package com.ivanpodlesnykh.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadTheme()

        handleButtons()
    }

    private fun loadTheme() {
        val sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE)
        if(sharedPreferences.getBoolean("DARK_THEME_KEY", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
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