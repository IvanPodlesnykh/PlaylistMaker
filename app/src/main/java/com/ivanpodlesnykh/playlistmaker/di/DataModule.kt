package com.ivanpodlesnykh.playlistmaker.di

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.gson.Gson
import com.ivanpodlesnykh.playlistmaker.data.converters.FavoriteTracksDbConverter
import com.ivanpodlesnykh.playlistmaker.data.db.FavoriteTracksDatabase
import com.ivanpodlesnykh.playlistmaker.data.search.network.MusicApi
import com.ivanpodlesnykh.playlistmaker.data.search.shared_preferences.SearchHistory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val baseUrl = "https://itunes.apple.com"

val dataModule = module {

    single<MusicApi> {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MusicApi::class.java)
    }

    factory {
        MediaPlayer()
    }

    single {
        androidContext()
            .getSharedPreferences("sharedPref", AppCompatActivity.MODE_PRIVATE)
    }

    single {
        Gson()
    }

    single {
        SearchHistory(get())
    }

    single {
        Room.databaseBuilder(androidContext(),
            FavoriteTracksDatabase::class.java,
            "favorite_tracks_database.db")
            .build()
    }

    factory {
        FavoriteTracksDbConverter()
    }
}