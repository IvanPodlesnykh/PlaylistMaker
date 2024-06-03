package com.ivanpodlesnykh.playlistmaker.domain.db.impl

import com.ivanpodlesnykh.playlistmaker.domain.db.api.FavoriteTracksDatabaseInteractor
import com.ivanpodlesnykh.playlistmaker.domain.db.api.FavoriteTracksDatabaseRepository
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksDatabaseInteractorImpl(
    private val favoriteTracksDatabaseRepository: FavoriteTracksDatabaseRepository
) : FavoriteTracksDatabaseInteractor {
    override suspend fun addTrackToFavorite(track: Track) {
        favoriteTracksDatabaseRepository.addTrackToFavorite(track)
    }

    override suspend fun deleteTrack(trackId: String) {
        favoriteTracksDatabaseRepository.deleteTrack(trackId)
    }

    override suspend fun getAllFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracksDatabaseRepository.getAllFavoriteTracks()
    }
}