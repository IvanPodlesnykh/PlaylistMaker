package com.ivanpodlesnykh.playlistmaker.data.media.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.core.net.toUri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ivanpodlesnykh.playlistmaker.data.converters.PlaylistDbConverter
import com.ivanpodlesnykh.playlistmaker.data.db.PlaylistsDatabase
import com.ivanpodlesnykh.playlistmaker.domain.media.api.PlaylistRepository
import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(
    private val playlistsDatabase: PlaylistsDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val context: Context,
    private val gson: Gson
) : PlaylistRepository {
    override suspend fun createPlaylist(playlist: Playlist) {
        playlistsDatabase.getPlaylistDao().addPlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistsDatabase.getPlaylistDao().updatePlaylist(playlistDbConverter.map(playlist))
    }

    override fun saveImageToPrivateStorage(uri: String): String {
        val uriParsed = uri.toUri()

        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PlaylistMakerAlbum")

        if (!filePath.exists()){
            filePath.mkdirs()
        }

        val fileName = System.currentTimeMillis()

        val file = File(filePath, "$fileName.jpg")

        val inputStream = context.contentResolver.openInputStream(uriParsed)

        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.toString()
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        emit (playlistsDatabase.getPlaylistDao().getAllPlaylists().map {
            playlistDbConverter.map(it)
        })
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistsDatabase.getPlaylistDao().addTrackToPlaylist(playlistDbConverter.map(track))

        val itemType = object : TypeToken<ArrayList<String>>() {}.type

        var listOfTracks = gson.fromJson<ArrayList<String>>(playlist.tracks, itemType)

        if(listOfTracks.isNullOrEmpty()) listOfTracks = ArrayList()

        listOfTracks.add(track.trackId)

        val json = gson.toJson(listOfTracks)

        val updatedPlaylist = Playlist(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            imageUri = playlist.imageUri,
            tracks = json,
            numberOfTracks = playlist.numberOfTracks + 1L
        )

        playlistsDatabase.getPlaylistDao().updatePlaylist(playlistDbConverter.map(updatedPlaylist))
    }
}