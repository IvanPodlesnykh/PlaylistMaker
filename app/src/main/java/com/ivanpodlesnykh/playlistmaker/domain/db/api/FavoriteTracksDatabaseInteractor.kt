package com.ivanpodlesnykh.playlistmaker.domain.db.api

import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksDatabaseInteractor {

    suspend fun addTrackToFavorite(track: Track)

    suspend fun deleteTrack(trackId: String)

    suspend fun getAllFavoriteTracks(): Flow<List<Track>>

}