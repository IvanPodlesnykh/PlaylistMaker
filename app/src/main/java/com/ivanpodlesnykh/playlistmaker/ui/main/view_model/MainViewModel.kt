package com.ivanpodlesnykh.playlistmaker.ui.main.view_model

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel

class MainViewModel(private val application: Application) : AndroidViewModel(application) {

    fun <T : Activity> openActivity(activityClass: Class<T>) {
        val intent = Intent(application, activityClass)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }
}