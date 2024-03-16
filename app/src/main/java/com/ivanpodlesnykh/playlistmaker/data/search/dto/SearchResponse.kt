package com.ivanpodlesnykh.playlistmaker.data.search.dto

import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track

class SearchResponse(val results: ArrayList<Track>)