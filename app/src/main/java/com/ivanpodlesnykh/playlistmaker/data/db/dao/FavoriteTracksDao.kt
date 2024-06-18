package com.ivanpodlesnykh.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ivanpodlesnykh.playlistmaker.data.db.entities.TrackEntity

@Dao
interface FavoriteTracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewTrack(track: TrackEntity)

    @Query("DELETE FROM favorite_tracks_table WHERE track_id = :trackId")
    suspend fun deleteTrack(trackId: String)

    @Query("SELECT * FROM favorite_tracks_table")
    suspend fun getAllTracks(): List<TrackEntity>

    @Query("SELECT track_id FROM favorite_tracks_table")
    suspend fun getAllTracksIds(): List<String>
}