package com.example.playlistmaker.search.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.mediateka.favorite.ui.SongItem
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.search.ui.view_model.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel
) {
    val state by viewModel.state.collectAsState()

    SearchScreen(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun SearchScreen(
    state: SearchState,
    onEvent: (event: SearchEvent) -> Unit
) {

    var search by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 14.dp)
    ) {

        Text(
            text = stringResource(id = R.string.search),
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            fontSize = 22.sp,
            color = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                id = R.color.black
            ),
            letterSpacing = 0.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        SearchField(value = search,
            onValueChange = { newSearch ->
                Log.d("TAG", "onValueChange = ${newSearch}")
                search = newSearch
                onEvent(SearchEvent.TextChangedInput(search))
            },
            onClearField = {
                search = ""
                onEvent(SearchEvent.ClearTextField)
            })


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (state) {
                SearchState.Default -> {}
                SearchState.HistoryEmpty -> {

                }

                is SearchState.HistoryNotEmpty -> {

                    Spacer(modifier = Modifier.height(50.dp))

                    Text(
                        text = stringResource(id = R.string.you_search),
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        fontSize = 19.sp,
                        color = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                            id = R.color.black
                        ),
                        letterSpacing = 0.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    LazyColumn {
                        items((state).tracks) { track ->
                            SongItem(
                                track = track, onClick = { onEvent(SearchEvent.ClickTrack(track)) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { onEvent(SearchEvent.ClearHistory) },
                        colors = ButtonColors(
                            containerColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                                id = R.color.black
                            ),
                            contentColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                                id = R.color.black
                            ),
                            disabledContentColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                                id = R.color.black
                            ),
                            disabledContainerColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                                id = R.color.black
                            )
                        ), contentPadding = PaddingValues(vertical = 10.dp, horizontal = 14.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.clear_history),
                            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                            fontSize = 14.sp,
                            letterSpacing = 0.sp,
                            color = if (isSystemInDarkTheme()) colorResource(id = R.color.black) else colorResource(
                                id = R.color.white
                            ),
                        )
                    }
                }

                SearchState.Loading -> {
                    Spacer(modifier = Modifier.height(148.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.size(44.dp),
                        color = colorResource(id = R.color.blue)
                    )
                }

                SearchState.SearchEmpty -> {
                    Spacer(modifier = Modifier.height(110.dp))
                    Image(
                        painter = painterResource(id = R.drawable.nothing_was_found),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.nothing_was_found),
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        fontSize = 19.sp,
                        color = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                            id = R.color.black
                        ),
                        letterSpacing = 0.sp
                    )
                }

                SearchState.SearchFail -> {
                    Spacer(modifier = Modifier.height(110.dp))
                    Image(
                        painter = painterResource(id = R.drawable.communication_problems),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.communication_problems_1),
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        fontSize = 19.sp,
                        color = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                            id = R.color.black
                        ),
                        letterSpacing = 0.sp
                    )

                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        text = stringResource(id = R.string.communication_problems_2),
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        fontSize = 19.sp,
                        color = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                            id = R.color.black
                        ),
                        letterSpacing = 0.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { onEvent(SearchEvent.RefreshSearch) },
                        colors = ButtonColors(
                            containerColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                                id = R.color.black
                            ),
                            contentColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                                id = R.color.black
                            ),
                            disabledContentColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                                id = R.color.black
                            ),
                            disabledContainerColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                                id = R.color.black
                            )
                        ), contentPadding = PaddingValues(vertical = 10.dp, horizontal = 14.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.refresh),
                            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                            fontSize = 14.sp,
                            letterSpacing = 0.sp,
                            color = if (isSystemInDarkTheme()) colorResource(id = R.color.black) else colorResource(
                                id = R.color.white
                            ),
                        )
                    }
                }

                is SearchState.SearchSuccess -> {
                    Spacer(modifier = Modifier.height(24.dp))
                    LazyColumn {
                        items((state).tracks) { track ->
                            SongItem(
                                track = track, onClick = { onEvent(SearchEvent.ClickTrack(track)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenSuccess() {
    val state: SearchState by remember {
        mutableStateOf(
            SearchState.SearchSuccess(
                listOf(
                    Track(
                        id = 1,
                        artistName = "The Beatles",
                        trackTimeMillis = null,
                        artworkUrl100 = null,
                        artworkUrl60 = null,
                        trackName = "Yesterday (Remastered 2009)",
                        collectionName = null,
                        releaseDate = null,
                        primaryGenreName = null,
                        country = null,
                        previewUrl = null,
                        isFavorite = false
                    ),
                    Track(
                        id = 1,
                        artistName = "The Beateeeeeeeeeles",
                        trackTimeMillis = null,
                        artworkUrl100 = null,
                        artworkUrl60 = null,
                        trackName = "Here Comes The Sun (Remastereeeeed...",
                        collectionName = null,
                        releaseDate = null,
                        primaryGenreName = null,
                        country = null,
                        previewUrl = null,
                        isFavorite = false
                    ),
                )
            )
        )
    }
    Column(modifier = Modifier.fillMaxSize()) {
        SearchScreen(state = state, {})
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenHistory() {
    val state: SearchState by remember {
        mutableStateOf(
            SearchState.HistoryNotEmpty(
                listOf(
                    Track(
                        id = 1,
                        artistName = "The Beatles",
                        trackTimeMillis = null,
                        artworkUrl100 = null,
                        artworkUrl60 = null,
                        trackName = "Yesterday (Remastered 2009)",
                        collectionName = null,
                        releaseDate = null,
                        primaryGenreName = null,
                        country = null,
                        previewUrl = null,
                        isFavorite = false
                    ),
                    Track(
                        id = 1,
                        artistName = "The Beateeeeeeeeeles",
                        trackTimeMillis = null,
                        artworkUrl100 = null,
                        artworkUrl60 = null,
                        trackName = "Here Comes The Sun (Remastereeeeed...",
                        collectionName = null,
                        releaseDate = null,
                        primaryGenreName = null,
                        country = null,
                        previewUrl = null,
                        isFavorite = false
                    ),
                )
            )
        )
    }
    Column(modifier = Modifier.fillMaxSize()) {
        SearchScreen(state = state, {})
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenLoading() {
    val state: SearchState by remember {
        mutableStateOf(SearchState.Loading)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        SearchScreen(state = state, {})
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenInet() {
    val state: SearchState by remember {
        mutableStateOf(SearchState.SearchFail)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        SearchScreen(state = state, {})
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenSearchEmpty() {
    val state: SearchState by remember {
        mutableStateOf(SearchState.SearchEmpty)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        SearchScreen(state = state, {})
    }
}
