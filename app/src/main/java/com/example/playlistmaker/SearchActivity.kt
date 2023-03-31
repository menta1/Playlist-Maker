package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var inputEditText: EditText
    private lateinit var backButton: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var communicationProblems: LinearLayout
    private lateinit var nothingWasFound: LinearLayout
    private lateinit var refreshButton: Button
    private lateinit var clearHistory: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerLayout: FrameLayout
    private val adapter = TrackAdapter()
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)
    private val tracks = ArrayList<Track>()

    companion object{
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

        adapter.tracks = tracks
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter



        backButton.setOnClickListener{
            finish()
        }

        clearButton.setOnClickListener{
            inputEditText.setText("")
            it.hideKeyboard()
        }

        var searchTextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        inputEditText.addTextChangedListener(searchTextWatcher)

        refreshButton.setOnClickListener{
            iTunesServiceSearch()
        }

        clearHistory.setOnClickListener{
            inputEditText.setText("")
            it.hideKeyboard()
            tracks.clear()
            adapter.notifyDataSetChanged()
            clearHistory.visibility = View.GONE
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

    }

    fun iTunesServiceSearch(){
        if(inputEditText.text.isNotEmpty()){
            iTunesService.search(inputEditText.text.toString()).enqueue(object : Callback<TrackResponse>{
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ){
                    if (response.code() == 200){
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true){
                            progressBar.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                            recyclerLayout.visibility = View.VISIBLE
                            clearHistory.visibility = View.VISIBLE
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if(tracks.isEmpty()){
                            visibleContent(false)
                        }

                    }else{
                        visibleContent(false)
                    }

                }
                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    visibleContent(true)
                }
            })
        }
    }

    fun visibleContent(fail: Boolean){
        if(fail) {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.GONE
            recyclerLayout.visibility = View.GONE
            nothingWasFound.visibility = View.GONE
            communicationProblems.visibility = View.VISIBLE
            refreshButton.visibility = View.VISIBLE
        }else{
            refreshButton.visibility = View.GONE
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.GONE
            recyclerLayout.visibility = View.GONE
            nothingWasFound.visibility = View.VISIBLE
        }
    }
    fun clearButtonVisibility(s: CharSequence?) : Int{
        return if(s.isNullOrEmpty()){
            View.GONE
        }else{
            View.VISIBLE
        }
    }
    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}