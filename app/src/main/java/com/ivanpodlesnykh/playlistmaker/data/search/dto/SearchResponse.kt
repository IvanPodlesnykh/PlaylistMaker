package com.ivanpodlesnykh.playlistmaker.data.search.dto

import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track

class SearchResponse(val results: List<Track>, val code: String)