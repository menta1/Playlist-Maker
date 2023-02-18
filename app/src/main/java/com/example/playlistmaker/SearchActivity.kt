package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    lateinit var inputEditText: EditText
    lateinit var backButton: ImageView
    lateinit var clearButton: ImageView
    companion object{
        const val TEXT_EDITTEXT = "TEXT_EDITTEXT"
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