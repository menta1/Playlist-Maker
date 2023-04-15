package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), TrackAdapter.Listener {
    private lateinit var inputEditText: EditText
    private lateinit var backButton: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var communicationProblems: LinearLayout
    private lateinit var nothingWasFound: LinearLayout
    private lateinit var refreshButton: Button
    private lateinit var clearHistory: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerLayout: LinearLayout
    private lateinit var youSearch: TextView
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private val adapter = TrackAdapter(this)

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)

    companion object {
        const val TEXT_EDITTEXT = "TEXT_EDITTEXT"
        const val iTunesBaseUrl = "https://itunes.apple.com"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_EDITTEXT, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputEditText.setText(savedInstanceState.getString(TEXT_EDITTEXT))
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        backButton = findViewById(R.id.backButton)
        inputEditText = findViewById(R.id.inputEditText)
        clearButton = findViewById(R.id.clearButton)
        recyclerView = findViewById(R.id.recyclerSongItem)
        communicationProblems = findViewById(R.id.placeholder_communication_problems)
        nothingWasFound = findViewById(R.id.placeholder_nothing_was_found)
        refreshButton = findViewById(R.id.refresh_button)
        clearHistory = findViewById(R.id.clear_history)
        progressBar = findViewById(R.id.progressBar)
        recyclerLayout = findViewById(R.id.recycler_layout)
        youSearch = findViewById(R.id.you_search)
        sharedPrefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            it.hideKeyboard()
            adapter.setTracks(searchHistory.showTrackHistory())
            progressBar.visibility = View.GONE
            nothingWasFound.visibility = View.GONE
            communicationProblems.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerLayout.visibility = View.VISIBLE
            clearHistory.visibility = View.VISIBLE
            if (searchHistory.showTrackHistory().isEmpty()) {
                youSearch.visibility = View.GONE
                clearHistory.visibility = View.GONE
            }

        }

        var searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                youSearch.visibility = if (searchHistory.showTrackHistory().isNotEmpty()) {
                    View.VISIBLE
                } else if (inputEditText.hasFocus() && s?.isEmpty() == true)
                    View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        inputEditText.addTextChangedListener(searchTextWatcher)

        refreshButton.setOnClickListener {
            iTunesServiceSearch()
        }

        clearHistory.setOnClickListener {
            inputEditText.setText("")
            it.hideKeyboard()
            clearHistory.visibility = View.GONE
            youSearch.visibility = View.GONE
            adapter.setTracks(searchHistory.clearHistory())

        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                progressBar.visibility = View.VISIBLE
                communicationProblems.visibility = View.GONE
                nothingWasFound.visibility = View.GONE
                iTunesServiceSearch()
            }
            false
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (searchHistory.showTrackHistory().isNotEmpty()) {
                youSearch.visibility = if (hasFocus && inputEditText.text.isEmpty())
                    View.VISIBLE else View.GONE
                if (youSearch.visibility == View.VISIBLE) {
                    adapter.setTracks(searchHistory.showTrackHistory())
                    recyclerView.visibility = View.VISIBLE
                    clearHistory.visibility = View.VISIBLE
                } else {
                    adapter.setTracks(searchHistory.showTrackHistory())
                }
            }
        }

    }

    private fun iTunesServiceSearch() {
        if (inputEditText.text.isNotEmpty()) {
            iTunesService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                Log.d("tag", response.body()?.results.toString())
                                progressBar.visibility = View.GONE
                                recyclerView.visibility = View.VISIBLE
                                recyclerLayout.visibility = View.VISIBLE
                                adapter.setTracks(response.body()?.results!!)
                                youSearch.visibility = View.GONE
                                clearHistory.visibility = View.GONE
                            }
                        } else {
                            visibleContent(false)
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        visibleContent(true)
                    }
                })
        }
    }

    fun visibleContent(fail: Boolean) {
        if (fail) {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.GONE
            recyclerLayout.visibility = View.GONE
            nothingWasFound.visibility = View.GONE
            communicationProblems.visibility = View.VISIBLE
            refreshButton.visibility = View.VISIBLE
        } else {
            refreshButton.visibility = View.GONE
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.GONE
            recyclerLayout.visibility = View.GONE
            nothingWasFound.visibility = View.VISIBLE
        }
    }

    fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onClick(track: Track) {
        searchHistory.addTrackHistory(track)
        val audioplayer = Intent(this, AudioplayerActivity::class.java)
        audioplayer.putExtra("TRACK", track)
        startActivity(audioplayer)
    }
}