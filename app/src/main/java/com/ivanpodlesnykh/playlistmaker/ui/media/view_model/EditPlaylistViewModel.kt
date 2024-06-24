package com.ivanpodlesnykh.playlistmaker.ui.media.view_model

import androidx.lifecycle.viewModelScope
import com.ivanpodlesnykh.playlistmaker.domain.media.api.PlaylistInteractor
import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlist: Playlist
) : CreatePlaylistViewModel(playlistInteractor) {

    override fun createPlaylist(title: String, description: String) {



        viewModelScope.launch(Dispatchers.IO) {

            val savedUri = if(!getImageUri().value.isNullOrEmpty()) {
                playlistInteractor.saveImageToPrivateStorage(getImageUri().value!!)
            }
            else playlist.imageUri



            playlistInteractor.updatePlaylist(
                Playlist(
                    id = playlist.id,
                    title = title,
                    description = description,
                    imageUri = savedUri!!,
                    tracks = playlist.tracks,
                    numberOfTracks = playlist.numberOfTracks
                )
            )
        }
    }

}