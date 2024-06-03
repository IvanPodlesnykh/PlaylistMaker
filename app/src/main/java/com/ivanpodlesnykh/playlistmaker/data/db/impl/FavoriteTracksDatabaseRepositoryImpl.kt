package com.ivanpodlesnykh.playlistmaker.data.db.impl

import com.ivanpodlesnykh.playlistmaker.data.converters.FavoriteTracksDbConverter
import com.ivanpodlesnykh.playlistmaker.data.db.FavoriteTracksDatabase
import com.ivanpodlesnykh.playlistmaker.domain.db.api.FavoriteTracksDatabaseRepository
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksDatabaseRepositoryImpl(
    private val database: FavoriteTracksDatabase,
    private val favoriteTracksDbConverter: FavoriteTracksDbConverter
) : FavoriteTracksDatabaseRepository {
    override suspend fun addTrackToFavorite(track: Track) {
        database.getFavoriteTracksDao().insertNewTrack(favoriteTracksDbConverter.map(track))
    }

    override suspend fun deleteTrack(trackId: String) {
        database.getFavoriteTracksDao().deleteTrack(trackId)
    }

    override suspend fun getAllFavoriteTracks(): Flow<List<Track>> = flow {
        emit(database.getFavoriteTracksDao().getAllTracks().map {
            favoriteTracksDbConverter.map(it).also {
                it.isFavorite = true
            }
        })
    }
}