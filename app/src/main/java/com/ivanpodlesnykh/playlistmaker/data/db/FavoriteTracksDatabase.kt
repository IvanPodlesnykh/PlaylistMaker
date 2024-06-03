package com.ivanpodlesnykh.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ivanpodlesnykh.playlistmaker.data.db.dao.FavoriteTracksDao
import com.ivanpodlesnykh.playlistmaker.data.db.entities.TrackEntity

@Database(
    version = 1,
    entities = [
        TrackEntity::class
    ]
)
abstract class FavoriteTracksDatabase : RoomDatabase() {

    abstract fun getFavoriteTracksDao() : FavoriteTracksDao
}