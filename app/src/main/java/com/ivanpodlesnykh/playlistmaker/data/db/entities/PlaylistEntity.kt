package com.ivanpodlesnykh.playlistmaker.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playlist_id")
    val playlistId: Long = 0,
    @ColumnInfo(name = "playlist_title")
    val playlistTitle: String,
    @ColumnInfo(name = "playlist_description")
    val playlistDescription: String,
    @ColumnInfo(name = "playlist_cover_uri")
    val playlistCoverUri: String,

    val tracks: String,
    @ColumnInfo(name = "number_of_tracks")
    val numberOfTracks: Long
)