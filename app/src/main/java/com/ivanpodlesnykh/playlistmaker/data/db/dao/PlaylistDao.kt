package com.ivanpodlesnykh.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ivanpodlesnykh.playlistmaker.data.db.entities.PlaylistEntity
import com.ivanpodlesnykh.playlistmaker.data.db.entities.TrackInPlaylistsEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Update()
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists_table")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(track: TrackInPlaylistsEntity)

    @Query("SELECT tracks FROM playlists_table WHERE playlist_id = :playlistId")
    suspend fun getAllTracksIds(playlistId: Long): String

    @Query("SELECT * FROM tracks_in_playlists")
    suspend fun getAllTracks(): List<TrackInPlaylistsEntity>

    @Query("SELECT * FROM playlists_table WHERE playlist_id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity

    @Query("DELETE FROM tracks_in_playlists WHERE track_id = :trackId")
    suspend fun deleteTrackFromPlaylists(trackId: String)

    @Query("DELETE FROM playlists_table WHERE playlist_id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)

    @Query("UPDATE tracks_in_playlists SET is_favorite = :isFavorite WHERE track_id = :trackId")
    suspend fun updateFavoriteTrack(isFavorite: Boolean, trackId: String)

}