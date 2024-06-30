package com.ivanpodlesnykh.playlistmaker.domain.media.api

import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun saveImageToPrivateStorage(uri: String): String

    suspend fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    suspend fun getAllTracksFromPlaylist(playlistId: Long): Flow<List<Track>>

    suspend fun getPlaylist(playlistId: Long): Playlist

    suspend fun deleteTrack(playlistId: Long, trackId: String)

    suspend fun deletePlaylist(playlistId: Long)

    suspend fun sharePlaylist(playlistId: Long)
}