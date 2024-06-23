package com.ivanpodlesnykh.playlistmaker.data.converters

import com.ivanpodlesnykh.playlistmaker.data.db.entities.PlaylistEntity
import com.ivanpodlesnykh.playlistmaker.data.db.entities.TrackInPlaylistsEntity
import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track

class PlaylistDbConverter {

    fun map(playlist: Playlist): PlaylistEntity {
        if(playlist.id != null) {
            return PlaylistEntity(
                    playlistId = playlist.id,
                    playlistTitle = playlist.title,
                    playlistDescription = playlist.description,
                    playlistCoverUri = playlist.imageUri,
                    tracks = playlist.tracks,
                    numberOfTracks = playlist.numberOfTracks
                )
            }
        else {
            return PlaylistEntity(
                playlistTitle = playlist.title,
                playlistDescription = playlist.description,
                playlistCoverUri = playlist.imageUri,
                tracks = playlist.tracks,
                numberOfTracks = playlist.numberOfTracks
            )
        }
    }

    fun map (playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            id = playlistEntity.playlistId,
            title = playlistEntity.playlistTitle,
            description = playlistEntity.playlistDescription,
            imageUri = playlistEntity.playlistCoverUri,
            tracks = playlistEntity.tracks,
            numberOfTracks = playlistEntity.numberOfTracks
        )
    }

    fun map(track: Track): TrackInPlaylistsEntity {
        return TrackInPlaylistsEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }

    fun map(trackInPlaylist: TrackInPlaylistsEntity): Track {
        return Track(
            trackId = trackInPlaylist.trackId,
            trackName = trackInPlaylist.trackName,
            artistName = trackInPlaylist.artistName,
            trackTimeMillis = trackInPlaylist.trackTimeMillis,
            artworkUrl100 = trackInPlaylist.artworkUrl100,
            collectionName = trackInPlaylist.collectionName,
            releaseDate = trackInPlaylist.releaseDate,
            primaryGenreName = trackInPlaylist.primaryGenreName,
            country = trackInPlaylist.country,
            previewUrl = trackInPlaylist.previewUrl
        )
    }
}