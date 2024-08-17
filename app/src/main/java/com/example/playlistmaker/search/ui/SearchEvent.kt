package com.example.playlistmaker.search.ui

import com.example.playlistmaker.player.domain.model.Track


sealed class Clear: SearchEvent{
    data object ClearHistory : Clear()
    data object ClearTextField : Clear()
}
sealed class Search: SearchEvent{
    class ClickTrack(val track: Track) : Search()
    data object RefreshSearch : Search()
    class TextChangedInput(val text: String) : Search()
}

sealed interface SearchEvent