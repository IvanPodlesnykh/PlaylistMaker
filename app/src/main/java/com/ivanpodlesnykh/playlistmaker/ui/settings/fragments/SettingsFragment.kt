package com.ivanpodlesnykh.playlistmaker.ui.settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ivanpodlesnykh.playlistmaker.databinding.FragmentSettingsBinding
import com.ivanpodlesnykh.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModel<SettingsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleSwitch()

        handleShareButton()

        handleSupportButton()

        handleUserAgreementButton()
    }

    private fun handleSwitch() {
        if (viewModel.isNightMode()) {
            binding.themeSwitch.toggle()
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
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