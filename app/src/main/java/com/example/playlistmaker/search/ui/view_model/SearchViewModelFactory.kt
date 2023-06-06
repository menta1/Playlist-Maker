package com.example.playlistmaker.search.ui.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl

class SearchViewModelFactory(context: Context) : ViewModelProvider.Factory {


    private val getSearchRepository by lazy(LazyThreadSafetyMode.NONE) {
        SearchRepositoryImpl(context = context)
    }
    private val getSearchInteractor by lazy(LazyThreadSafetyMode.NONE) {
        SearchInteractorImpl(getSearchRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            getSearchInteractor
        ) as T
    }
}