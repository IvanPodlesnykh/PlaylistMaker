package com.ivanpodlesnykh.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var errorView: ViewGroup

    private lateinit var errorIcon: ImageView

    private lateinit var errorText: TextView

    private lateinit var reloadButton: Button

    private var searchString: String = SEARCH_STRING_DEF

    private val baseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val MusicService = retrofit.create(MusicApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        errorIcon = findViewById(R.id.errorIcon)
        errorView = findViewById(R.id.errorView)
        errorText = findViewById(R.id.errorText)
        reloadButton = findViewById(R.id.reloadButton)

        handleErrors(ErrorType.HIDE_ERROR)

        handleBackButton()

        handleTextInput()

        handleSearchHistory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING_KEY, searchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState != null) {
            val textInput = findViewById<EditText>(R.id.search_bar)
            textInput.setText(savedInstanceState.getString(SEARCH_STRING_KEY, SEARCH_STRING_DEF))
        }
    }

    private fun handleErrors(errorType: ErrorType) {
        when(errorType) {
            ErrorType.NO_CONNECTION -> {
                errorView.visibility = View.VISIBLE
                errorText.visibility = View.VISIBLE
                reloadButton.visibility = View.VISIBLE
                errorIcon.setBackgroundResource(R.drawable.no_connection)
                errorText.setText(resources.getString(R.string.error_message))
            }
            ErrorType.NOT_FOUND -> {
                errorView.visibility = View.VISIBLE
                errorText.visibility = View.VISIBLE
                errorIcon.setBackgroundResource(R.drawable.not_found)
                errorText.setText(resources.getString(R.string.not_found_message))
                reloadButton.visibility = View.GONE
            }
            ErrorType.HIDE_ERROR -> {
                errorView.visibility = View.GONE
                errorText.visibility = View.GONE
                reloadButton.visibility = View.GONE
            }
        }
    }

    private fun handleReloadButton(text: String) {
        reloadButton.setOnClickListener {
            MusicService.search(text)
                .enqueue(object : Callback<SearchResponse> {
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>
                    ) {
                        if (response.body()?.results!!.isEmpty()) handleErrors(ErrorType.NOT_FOUND)
                        else {
                            when(response.code()){
                                200 -> {
                                    handleTrackList(response.body()?.results!!)
                                    handleErrors(ErrorType.HIDE_ERROR)
                                }
                                else -> {
                                    handleErrors(ErrorType.NO_CONNECTION)
                                }
                            }
                        }
                    }
                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        handleErrors(ErrorType.NO_CONNECTION)
                    }
                })
        }
    }

    private fun handleBackButton() {
        val backButton = findViewById<ImageView>(R.id.search_back_button)
        backButton.setOnClickListener{
            this.finish()
        }
    }

    private fun handleTextInput() {

        handleErrors(ErrorType.HIDE_ERROR)

        val textInput = findViewById<EditText>(R.id.search_bar)

        val searchHistory = findViewById<ScrollView>(R.id.searchHistory)

        val sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE)

        val clearButton = findViewById<ImageView>(R.id.clear_search_button)
        clearButton.visibility = View.GONE

        clearButton.setOnClickListener {
            textInput.setText("")
            val view = currentFocus
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
            handleTrackList(arrayListOf())
            handleErrors(ErrorType.HIDE_ERROR)
            textInput.clearFocus()
        }

        val textWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                    searchHistory.isVisible = sharedPreferences.getString("SEARCH_HISTORY", null) != null
                } else {
                    clearButton.visibility = View.VISIBLE
                    searchHistory.isVisible = false
                }

                searchString = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        textInput.addTextChangedListener(textWatcher)

        textInput.setOnEditorActionListener { _, actionId, _ ->
            handleErrors(ErrorType.HIDE_ERROR)
            handleTrackList(arrayListOf())
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchHistory.visibility = View.GONE
                MusicService.search(textInput.text.toString())
                    .enqueue(object : Callback<SearchResponse> {
                        override fun onResponse(
                            call: Call<SearchResponse>,
                            response: Response<SearchResponse>
                        ) {
                            if (response.body()?.results!!.isEmpty()) handleErrors(ErrorType.NOT_FOUND)
                            else {
                                when(response.code()){
                                    200 -> {
                                        handleTrackList(response.body()?.results!!)
                                        handleErrors(ErrorType.HIDE_ERROR)
                                    }
                                    else -> {
                                        handleErrors(ErrorType.NO_CONNECTION)
                                        handleReloadButton(textInput.text.toString())
                                    }
                                }
                            }
                        }
                        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                            handleErrors(ErrorType.NO_CONNECTION)
                            handleReloadButton(textInput.text.toString())
                        }
                    })
                true
            }
            false
        }
    }

    private fun handleTrackList(trackList: ArrayList<Track>) {
        val recyclerView = findViewById<RecyclerView>(R.id.list_of_tracks)
        recyclerView.adapter = TrackAdapter(trackList)
    }

    private fun handleSearchHistory() {
        val textInput = findViewById<EditText>(R.id.search_bar)
        val searchHistoryView = findViewById<ScrollView>(R.id.searchHistory)
        val clearButton = findViewById<Button>(R.id.clear_search_history_button)
        val recyclerView = findViewById<RecyclerView>(R.id.searchHistoryList)

        val sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPreferences)

        val adapter = TrackAdapter(searchHistory.getTrackList())

        recyclerView.adapter = TrackAdapter(searchHistory.getTrackList())

        clearButton.setOnClickListener {
            searchHistory.clearTrackList()
            adapter.notifyDataSetChanged()
            val view = currentFocus
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
            searchHistoryView.visibility = View.GONE
            textInput.clearFocus()
        }

        textInput.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus && textInput.text.isEmpty() && sharedPreferences.getString("SEARCH_HISTORY", null) != null) {
                searchHistoryView.visibility = View.VISIBLE
                recyclerView.adapter = TrackAdapter(searchHistory.getTrackList())
            }
            else searchHistoryView.visibility = View.GONE
        }
    }

    companion object {
        private const val SEARCH_STRING_KEY = "SEARCH_INPUT"
        private const val SEARCH_STRING_DEF = ""
    }
}

enum class ErrorType {
    NO_CONNECTION,
    NOT_FOUND,
    HIDE_ERROR
}