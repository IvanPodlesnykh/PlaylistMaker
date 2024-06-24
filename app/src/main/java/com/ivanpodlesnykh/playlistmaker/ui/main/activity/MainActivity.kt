package com.ivanpodlesnykh.playlistmaker.ui.main.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.createPlaylistFragment -> {
                    binding.bottomNavigationView.isVisible = false
                    binding.separator.isVisible = false
                }
                R.id.playerFragment -> {
                    binding.bottomNavigationView.isVisible = false
                    binding.separator.isVisible = false
                }
                R.id.playlistFragment -> {
                    binding.bottomNavigationView.isVisible = false
                    binding.separator.isVisible = false
                }
                else -> {
                    binding.bottomNavigationView.isVisible = true
                    binding.separator.isVisible = true
                }
            }
        }
    }
}