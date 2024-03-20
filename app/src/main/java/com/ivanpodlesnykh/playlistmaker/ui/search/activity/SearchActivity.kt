package com.ivanpodlesnykh.playlistmaker.ui.search.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.databinding.ActivitySearchBinding
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.domain.search.models.ErrorType
import com.ivanpodlesnykh.playlistmaker.domain.search.models.SearchState
import com.ivanpodlesnykh.playlistmaker.ui.search.TrackAdapter
import com.ivanpodlesnykh.playlistmaker.ui.search.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private var searchString: String = SEARCH_STRING_DEF

    private lateinit var binding: ActivitySearchBinding

    private lateinit var viewModel: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        binding.loadingProgressBar.isVisible = false
        binding.clearSearchButton.isVisible = false

        viewModel.getSearchStateLiveData().observe(this) {
            when (it) {
                is SearchState.Default -> {
                    handleErrors(ErrorType.HIDE_ERROR)
                    binding.loadingProgressBar.isVisible = false
                }

                is SearchState.Error -> {
                    handleErrors(it.errorType)
                    binding.loadingProgressBar.isVisible = false
                    if (it.errorType == ErrorType.NO_CONNECTION) {
                        handleReloadButton(binding.searchBar.text.toString())
                    }
                }

                is SearchState.Loading -> {
                    handleErrors(ErrorType.HIDE_ERROR)
                    binding.loadingProgressBar.isVisible = true
                }

                is SearchState.ShowHistory -> {
                    handleErrors(ErrorType.HIDE_ERROR)
                    binding.loadingProgressBar.isVisible = false
                    showSearchHistory(it.trackList)
                }

                is SearchState.ShowTrackList -> {
                    handleErrors(ErrorType.HIDE_ERROR)
                    binding.loadingProgressBar.isVisible = false
                    val adapter = TrackAdapter(it.trackList)
                    binding.listOfTracks.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
        }

        handleBackButton()

        handleTextInput()

        prepareSearchHistory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING_KEY, searchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            val textInput = findViewById<EditText>(R.id.search_bar)
            textInput.setText(savedInstanceState.getString(SEARCH_STRING_KEY, SEARCH_STRING_DEF))
        }
    }

    private fun handleErrors(errorType: ErrorType) {
        when (errorType) {
            ErrorType.NO_CONNECTION -> {
                binding.errorView.isVisible = true
                binding.errorText.isVisible = true
                binding.reloadButton.isVisible = true
                binding.errorIcon.setBackgroundResource(R.drawable.no_connection)
                binding.errorText.text = resources.getString(R.string.error_message)
            }

            ErrorType.NOT_FOUND -> {
                binding.errorView.isVisible = true
                binding.errorText.isVisible = true
                binding.errorIcon.setBackgroundResource(R.drawable.not_found)
                binding.errorText.text = resources.getString(R.string.not_found_message)
                binding.reloadButton.isVisible = false
            }

            ErrorType.HIDE_ERROR -> {
                binding.errorView.isVisible = false
                binding.errorText.isVisible = false
                binding.reloadButton.isVisible = false
            }
        }
    }

    private fun handleReloadButton(text: String) {
        binding.reloadButton.setOnClickListener {
            viewModel.searchRequest(text)
        }
    }

    private fun handleBackButton() {
        binding.searchBackButton.setOnClickListener {
            this.finish()
        }
    }

    private fun handleTextInput() {

        binding.clearSearchButton.setOnClickListener {
            binding.searchBar.setText("")
            val view = currentFocus
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
            handleTrackList(arrayListOf())
            handleErrors(ErrorType.HIDE_ERROR)
            binding.searchBar.clearFocus()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.clearSearchButton.isVisible = false
                    binding.listOfTracks.isVisible = false
                    viewModel.searchDebounce(s.toString())
                    handleTrackList(arrayListOf())
                    handleErrors(ErrorType.HIDE_ERROR)
                } else {
                    binding.clearSearchButton.isVisible = true
                    binding.listOfTracks.isVisible = true
                    binding.searchHistory.isVisible = false
                    viewModel.searchDebounce(s.toString())
                    handleErrors(ErrorType.HIDE_ERROR)
                }

                searchString = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        binding.searchBar.addTextChangedListener(textWatcher)
    }

    private fun handleTrackList(trackList: List<Track>) {
        binding.listOfTracks.adapter = TrackAdapter(trackList)
    }

    private fun prepareSearchHistory() {
        binding.clearSearchHistoryButton.setOnClickListener {
            val adapter = TrackAdapter(emptyList())
            binding.searchHistoryList.adapter = adapter
            adapter.notifyDataSetChanged()

            viewModel.clearSearchHistory()
            val view = currentFocus
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
            binding.searchHistory.isVisible = false
            binding.searchBar.clearFocus()
        }

        binding.searchBar.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus && binding.searchBar.text.isEmpty() &&
                !viewModel.isSearchHistoryEmpty()) {
                viewModel.getSearchHistory()
            }
        }
    }

    private fun showSearchHistory(trackList: List<Track>) {
        val adapter = TrackAdapter(trackList)
        binding.searchHistoryList.adapter = adapter
        adapter.notifyDataSetChanged()

        binding.searchHistory.isVisible = true
    }

    companion object {
        private const val SEARCH_STRING_KEY = "SEARCH_INPUT"
        private const val SEARCH_STRING_DEF = ""
    }
}