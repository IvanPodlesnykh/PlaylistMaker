package com.ivanpodlesnykh.playlistmaker.domain.sharing.models

data class EmailData(
    val emailAddresses: Array<String>,
    val subject: String,
    val text: String)
