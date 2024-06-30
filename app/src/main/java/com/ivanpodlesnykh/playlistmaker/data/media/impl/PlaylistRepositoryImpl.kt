package com.ivanpodlesnykh.playlistmaker.data.media.impl

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.core.net.toUri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.data.converters.PlaylistDbConverter
import com.ivanpodlesnykh.playlistmaker.data.db.PlaylistsDatabase
import com.ivanpodlesnykh.playlistmaker.data.db.entities.PlaylistEntity
import com.ivanpodlesnykh.playlistmaker.domain.media.api.PlaylistRepository
import com.ivanpodlesnykh.playlistmaker.domain.media.models.Playlist
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

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

    override suspend fun saveImageToPrivateStorage(uri: String): String {

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

    override suspend fun getAllTracksFromPlaylist(playlistId: Long): Flow<List<Track>> {

        val tracksIdJson = playlistsDatabase.getPlaylistDao().getAllTracksIds(playlistId)

        val itemType = object : TypeToken<List<String>>() {}.type

        val listOfTrackIds = gson.fromJson<List<String>>(tracksIdJson, itemType)

        if(listOfTrackIds != null) {
            return flow {
                emit(
                playlistsDatabase.getPlaylistDao().getAllTracks()
                    .filter {
                        listOfTrackIds.contains(it.trackId)
                    }
                    .map {
                        playlistDbConverter.map(it)
                    }
                )
            }
        }
        else return flow {
            emit(emptyList())
        }
    }

    override suspend fun getPlaylist(playlistId: Long): Playlist {
        return playlistDbConverter.map(playlistsDatabase.getPlaylistDao().getPlaylistById(playlistId))
    }

    override suspend fun deleteTrack(playlistId: Long, trackId: String) {

        val playlist = playlistsDatabase.getPlaylistDao().getPlaylistById(playlistId)

        val itemType = object : TypeToken<ArrayList<String>>() {}.type

        val listOfTrackIds = gson.fromJson<ArrayList<String>>(playlist.tracks, itemType)

        listOfTrackIds.remove(trackId)

        val json = gson.toJson(listOfTrackIds)

        playlistsDatabase.getPlaylistDao().updatePlaylist(
            PlaylistEntity(
                playlistId = playlist.playlistId,
                playlistTitle = playlist.playlistTitle,
                playlistDescription = playlist.playlistDescription,
                playlistCoverUri = playlist.playlistCoverUri,
                tracks = json,
                numberOfTracks = playlist.numberOfTracks - 1
            )
        )

        deleteIfNotExists(trackId)
    }

    override suspend fun sharePlaylist(playlistId: Long) {

        val playlist = getPlaylist(playlistId)

        val numberOfTracks = playlist.numberOfTracks.toInt()

        var listOfTracks: List<Track> = emptyList()

        getAllTracksFromPlaylist(playlistId).collect{
            listOfTracks = it.reversed()
        }

        var tracks = ""

        for(track in listOfTracks) {
            tracks += "${listOfTracks.indexOf(track) + 1}.${track.artistName} - ${track.trackName} (${SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)})\n"
        }

        val text = "${playlist.title}\n${playlist.description}\n" + context.resources.getQuantityString(
            R.plurals.tracks_plurals, numberOfTracks, numberOfTracks) + "\n" +
            tracks

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.type = "text/plain"
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override suspend fun deletePlaylist(playlistId: Long) {

        val playlist = playlistsDatabase.getPlaylistDao().getPlaylistById(playlistId)

        playlistsDatabase.getPlaylistDao().deletePlaylist(playlistId)

        val itemType = object : TypeToken<ArrayList<String>>() {}.type

        val listOfTrackIds = gson.fromJson<ArrayList<String>>(playlist.tracks, itemType)

        if(!listOfTrackIds.isNullOrEmpty()) {
            for(id in listOfTrackIds) {
                deleteIfNotExists(id)
            }
        }
    }

    private suspend fun deleteIfNotExists(trackId: String) {

        val itemType = object : TypeToken<ArrayList<String>>() {}.type

        var exsists = false

        val allPlaylists = playlistsDatabase.getPlaylistDao().getAllPlaylists()

        for(currentPlaylist in allPlaylists) {
            val list = gson.fromJson<ArrayList<String>>(currentPlaylist.tracks, itemType)
            if(list != null && list.contains(trackId)) exsists = true
        }

        if(!exsists) playlistsDatabase.getPlaylistDao().deleteTrackFromPlaylists(trackId)

    }

}