package com.ivanpodlesnykh.playlistmaker.data.search.network

import com.ivanpodlesnykh.playlistmaker.data.search.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApi {

    @GET("/search?entity=song ")
    suspend fun search(@Query("term") text: String) : SearchResponse
}