package com.ivanpodlesnykh.playlistmaker.ui.settings.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.databinding.ActivitySettingsBinding
import com.ivanpodlesnykh.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        handleBackButton()

        handleSwitch()

        handleShareButton()

        handleSupportButton()

        handleUserAgreementButton()
    }

    private fun handleBackButton() {
        val backButton = findViewById<ImageView>(R.id.settings_back_button)
        backButton.setOnClickListener {
            this.finish()
        }
    }

    private fun handleSwitch() {
        if (viewModel.isNightMode()) {
            binding.themeSwitch.toggle()
        }

        binding.themeSwitch.setOnCheckedChangeListener { switcher, isChecked ->
            if (isChecked) {
                viewModel.updateTheme(true)
            } else {
                viewModel.updateTheme(false)
            }
        }
    }

    private fun handleShareButton() {
        binding.shareAppButton.setOnClickListener {
            viewModel.shareApp()
        }
    }

    private fun handleSupportButton() {
        binding.supportButton.setOnClickListener {
            viewModel.contactUserSupport()
        }
    }

    private fun handleUserAgreementButton() {
        binding.userAgreementButton.setOnClickListener {
            viewModel.openUserAgreement()
        }
    }
}

