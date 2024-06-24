package com.ivanpodlesnykh.playlistmaker.domain.media.impl

import com.ivanpodlesnykh.playlistmaker.domain.media.api.PlaylistInteractor
import com.ivanpodlesnykh.playlistmaker.domain.media.api.PlaylistRepository
import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist) {
        playlistRepository.createPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun saveImageToPrivateStorage(uri: String): String {
        return playlistRepository.saveImageToPrivateStorage(uri)
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun getAllTracksFromPlaylist(playlistId: Long): Flow<List<Track>> {
        return playlistRepository.getAllTracksFromPlaylist(playlistId)
    }

    override suspend fun getPlaylist(playlistId: Long): Playlist {
        return playlistRepository.getPlaylist(playlistId)
    }

    override suspend fun deleteTrack(playlistId: Long, trackId: String) {
        playlistRepository.deleteTrack(playlistId, trackId)
    }

    override suspend fun sharePlaylist(playlistId: Long) {
        playlistRepository.sharePlaylist(playlistId)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        playlistRepository.deletePlaylist(playlistId)
    }
}