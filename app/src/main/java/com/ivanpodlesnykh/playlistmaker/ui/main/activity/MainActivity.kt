package com.ivanpodlesnykh.playlistmaker.ui.main.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ivanpodlesnykh.playlistmaker.databinding.ActivityMainBinding
import com.ivanpodlesnykh.playlistmaker.ui.main.view_model.MainViewModel
import com.ivanpodlesnykh.playlistmaker.ui.media.activity.MediaActivity
import com.ivanpodlesnykh.playlistmaker.ui.search.activity.SearchActivity
import com.ivanpodlesnykh.playlistmaker.ui.settings.activity.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleButtons()
    }

    private fun handleButtons() {
        val searchButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.openActivity(SearchActivity::class.java)
            }
        }

        binding.searchButton.setOnClickListener(searchButtonClickListener)

        binding.mediaButton.setOnClickListener{
            viewModel.openActivity(MediaActivity::class.java)
        }

        binding.settingsButton.setOnClickListener{
            viewModel.openActivity(SettingsActivity::class.java)
        }
    }
}