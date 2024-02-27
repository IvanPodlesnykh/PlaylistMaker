package com.ivanpodlesnykh.playlistmaker.data.shared_preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ivanpodlesnykh.playlistmaker.domain.models.Track

class SearchHistory(val sharedPreferences: SharedPreferences) {

    private var trackList: ArrayList<Track> = arrayListOf()

    init {
        val json = sharedPreferences.getString("SEARCH_HISTORY", null)
        if(json != null) {
            val itemType = object : TypeToken<ArrayList<Track>>() {}.type
            trackList = Gson().fromJson<ArrayList<Track>>(json, itemType)
        }
    }

    fun saveTrackToList(track: Track) {
        var hasDuplicate = false

        var json = sharedPreferences.getString("SEARCH_HISTORY", null)
        if (json != null) {
            val itemType = object : TypeToken<ArrayList<Track>>() {}.type
            trackList = Gson().fromJson<ArrayList<Track>>(json, itemType)
        }

        for (item in trackList) {
            if(item.trackId == track.trackId) {
                trackList.remove(item)
                hasDuplicate = true
                break
            }
        }
        if(hasDuplicate) trackList.add(track)
        else {
            if(trackList.size < 10) trackList.add(track)
            else {
                trackList.removeAt(0)
                trackList.add(track)
            }
        }

        json = Gson().toJson(trackList)

        sharedPreferences.edit().putString("SEARCH_HISTORY", json).apply()
    }

    fun getTrackList(): ArrayList<Track> {
        val json = sharedPreferences.getString("SEARCH_HISTORY", null)
        if (json != null) {
            val itemType = object : TypeToken<ArrayList<Track>>() {}.type
            trackList = Gson().fromJson<ArrayList<Track>>(json, itemType)
            return trackList
        }
        return arrayListOf()
    }

    fun clearTrackList() {
        trackList.clear()

        val json = null

        sharedPreferences.edit().putString("SEARCH_HISTORY", json).apply()
    }
}