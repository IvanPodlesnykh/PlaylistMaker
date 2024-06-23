package com.ivanpodlesnykh.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ivanpodlesnykh.playlistmaker.data.db.dao.PlaylistDao
import com.ivanpodlesnykh.playlistmaker.data.db.entities.PlaylistEntity
import com.ivanpodlesnykh.playlistmaker.data.db.entities.TrackInPlaylistsEntity

@Database(
    version = 1,
    entities = [
        PlaylistEntity::class,
        TrackInPlaylistsEntity::class
    ]
)
abstract class PlaylistsDatabase : RoomDatabase() {

    abstract fun getPlaylistDao(): PlaylistDao
}