package com.example.playlistmaker.search.ui

import com.example.playlistmaker.player.domain.model.Track

sealed class SearchEvent {

    class ClickTrack(val track: Track) : SearchEvent()

    data object ClearHistory : SearchEvent()

    data object ClearTextField : SearchEvent()
    data object RefreshSearch : SearchEvent()

    class TextChangedInput(val text: String) : SearchEvent()

}