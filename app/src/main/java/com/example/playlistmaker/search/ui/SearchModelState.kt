package com.example.playlistmaker.search.ui

sealed class SearchModelState {
    object Loading : SearchModelState()
    object HistoryEmpty : SearchModelState()
    object HistoryNotEmpty : SearchModelState()
    object SearchSuccess : SearchModelState()
    object SearchFail : SearchModelState()
    object SearchEmpty : SearchModelState()
}