package com.ivanpodlesnykh.playlistmaker.ui.media.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.databinding.ActivityMediaBinding
import com.ivanpodlesnykh.playlistmaker.ui.media.MediaViewPagerAdapter

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding

    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleBackButton()

        handleTabsAndViewPager()
    }

    override fun onDestroy() {
        super.onDestroy()

        tabMediator.detach()
    }

    private fun handleBackButton() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun handleTabsAndViewPager() {
        binding.pager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabs, binding.pager) {
            tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }

        tabMediator.attach()
    }
}