package com.ivanpodlesnykh.playlistmaker.data.search.network

import com.ivanpodlesnykh.playlistmaker.data.search.dto.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApi {

    @GET("/search?entity=song ")
    fun search(@Query("term") text: String) : Call<SearchResponse>
}