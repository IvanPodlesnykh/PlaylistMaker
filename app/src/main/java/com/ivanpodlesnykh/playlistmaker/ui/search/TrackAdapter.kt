package com.ivanpodlesnykh.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.gson.Gson
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.data.search.shared_preferences.SearchHistory
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackAdapter(val trackList: List<Track>, private val coroutineScope: CoroutineScope, private val navController: NavController, private val destination: Int) : Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false))
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        val sharedPreferences = holder.itemView.context.getSharedPreferences("sharedPref",
            AppCompatActivity.MODE_PRIVATE
        )

        val searchHistory = SearchHistory(sharedPreferences)

        holder.itemView.setOnClickListener{

            holder.itemView.isEnabled = false

            coroutineScope.launch {
                delay(DEBOUNCER_TIME)
                holder.itemView.isEnabled = true
            }

            searchHistory.saveTrackToList(trackList[position])

            val json = Gson().toJson(trackList[position])

            navController.navigate(destination,
                bundleOf("TRACK" to json)
            )
        }

        holder.bind(trackList[position])
    }

    companion object {
        private const val DEBOUNCER_TIME = 1000L
    }
}