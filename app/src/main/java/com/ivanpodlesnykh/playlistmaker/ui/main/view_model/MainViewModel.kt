package com.ivanpodlesnykh.playlistmaker.ui.main.view_model

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class MainViewModel(private val application: Application) : AndroidViewModel(application) {

    fun <T : Activity> openActivity(activityClass: Class<T>) {
        val intent = Intent(application, activityClass)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    companion object {
        fun getMainViewModelFactory(application: Application) : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(application)
            }
        }
    }
}