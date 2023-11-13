package com.ivanpodlesnykh.playlistmaker

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    private var searchString: String = SEARCH_STRING_DEF

    companion object {
        const val SEARCH_STRING_KEY = "SEARCH_INPUT"
        const val SEARCH_STRING_DEF = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageView>(R.id.search_back_button)
        backButton.setOnClickListener{
            this.finish()
        }

        val textInput = findViewById<EditText>(R.id.search_bar)

        val clearButton = findViewById<ImageView>(R.id.clear_search_button)
        clearButton.visibility = View.GONE

        clearButton.setOnClickListener{
            textInput.setText("")
            val view = currentFocus
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        val textWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                } else clearButton.visibility = View.VISIBLE

                searchString = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        textInput.addTextChangedListener(textWatcher)
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
}