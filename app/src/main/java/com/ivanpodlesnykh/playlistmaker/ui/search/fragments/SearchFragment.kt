package com.ivanpodlesnykh.playlistmaker.ui.search.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ivanpodlesnykh.playlistmaker.R
import com.ivanpodlesnykh.playlistmaker.databinding.FragmentSearchBinding
import com.ivanpodlesnykh.playlistmaker.domain.player.models.Track
import com.ivanpodlesnykh.playlistmaker.domain.search.models.ErrorType
import com.ivanpodlesnykh.playlistmaker.domain.search.models.SearchState
import com.ivanpodlesnykh.playlistmaker.ui.search.TrackAdapter
import com.ivanpodlesnykh.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var searchString: String = SearchFragment.SEARCH_STRING_DEF

    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loadingProgressBar.isVisible = false
        binding.clearSearchButton.isVisible = false

        viewModel.getSearchStateLiveData().observe(viewLifecycleOwner) {
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
                    val adapter = TrackAdapter(it.trackList, lifecycleScope)
                    binding.listOfTracks.adapter = adapter
                    adapter.notifyDataSetChanged()
                }

                is SearchState.UpdateHistory -> {
                    updateSearchHistory(it.trackList)
                }
            }
        }

        handleTextInput()

        prepareSearchHistory()
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

    override fun onResume() {
        super.onResume()
        viewModel.updateSearchHistory()
    }

    private fun handleReloadButton(text: String) {
        binding.reloadButton.setOnClickListener {
            viewModel.searchRequest(text)
        }
    }

    private fun handleTextInput() {

        binding.clearSearchButton.setOnClickListener {
            binding.searchBar.setText("")
            val view = requireActivity().currentFocus
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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
                    viewModel.delaySearch(s.toString())
                    handleTrackList(arrayListOf())
                    handleErrors(ErrorType.HIDE_ERROR)
                } else {
                    binding.clearSearchButton.isVisible = true
                    binding.listOfTracks.isVisible = true
                    binding.searchHistory.isVisible = false
                    viewModel.delaySearch(s.toString())
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
        binding.listOfTracks.adapter = TrackAdapter(trackList, lifecycleScope)
    }

    private fun prepareSearchHistory() {
        binding.clearSearchHistoryButton.setOnClickListener {
            val adapter = TrackAdapter(emptyList(), lifecycleScope)
            binding.searchHistoryList.adapter = adapter
            adapter.notifyDataSetChanged()

            viewModel.clearSearchHistory()
            val view = requireActivity().currentFocus
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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
        val adapter = TrackAdapter(trackList, lifecycleScope)
        binding.searchHistoryList.adapter = adapter
        adapter.notifyDataSetChanged()

        binding.searchHistory.isVisible = true
    }

    private fun updateSearchHistory(trackList: List<Track>) {
        val adapter = TrackAdapter(trackList, lifecycleScope)
        binding.searchHistoryList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    companion object {
        private const val SEARCH_STRING_KEY = "SEARCH_INPUT"
        private const val SEARCH_STRING_DEF = ""
    }
}