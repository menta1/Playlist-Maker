package com.example.playlistmaker.mediateka

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.mediateka.favorite.ui.FavoriteScreen
import com.example.playlistmaker.mediateka.favorite.ui.FavoriteState
import com.example.playlistmaker.mediateka.favorite.ui.FavoritesTracksViewModel
import com.example.playlistmaker.mediateka.playlist.ui.PlaylistScreen
import com.example.playlistmaker.mediateka.playlist.ui.PlaylistState
import com.example.playlistmaker.mediateka.playlist.ui.PlaylistViewModel
import com.example.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@Composable
fun MediatekaScreen(
    onClickCreatePlaylist: () -> Unit,
    onClickCurrentPlaylist: (playlist: Playlist) -> Unit
) {
    val viewModelFavorite = koinViewModel<FavoritesTracksViewModel>()
    val viewModelPlaylist  = koinViewModel<PlaylistViewModel>()
    val stateFavorite by viewModelFavorite.state.collectAsState()
    val statePlaylist by viewModelPlaylist.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModelPlaylist.getPlaylists()
        viewModelFavorite.getAllTracksFavorite()
    }

    MediatekaScreen(
        stateFavorite = stateFavorite,
        statePlaylist = statePlaylist,
        onClickCreatePlaylist = onClickCreatePlaylist,
        onClickCurrentPlaylist = onClickCurrentPlaylist,
        onClickPlayer = viewModelFavorite::onClick
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediatekaScreen(
    stateFavorite: FavoriteState,
    statePlaylist: PlaylistState,
    onClickCreatePlaylist: () -> Unit,
    onClickCurrentPlaylist: (playlist: Playlist) -> Unit,
    onClickPlayer: (track: Track) -> Unit
) {

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val selectedTabIndex = remember { pagerState.currentPage }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 14.dp)
    ) {
        Text(
            text = stringResource(id = R.string.mediateka),
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            fontSize = 22.sp,
            color =
            if(isSystemInDarkTheme()) {
                 colorResource(id = R.color.white)
            } else colorResource(
                id = R.color.black
            ),
            letterSpacing = 0.sp
        )

        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedTabIndex,
            containerColor =
            if (isSystemInDarkTheme()) {
                colorResource(id = R.color.black)
            } else colorResource(
                id = R.color.white
            ),
            divider = { HorizontalDivider(color = Color.Transparent) },
            indicator = { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color =
                        if (isSystemInDarkTheme()) {
                            colorResource(id = R.color.white)
                        } else colorResource(
                            id = R.color.black
                        )
                    )
                }
            },
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                selectedContentColor =
                if (isSystemInDarkTheme()) {
                    colorResource(id = R.color.white)
                } else colorResource(
                    id = R.color.black
                ),
                unselectedContentColor =
                if (isSystemInDarkTheme()) {
                    colorResource(id = R.color.white)
                } else colorResource(
                    id = R.color.black
                ),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.favorites_tracks),
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        fontSize = 14.sp,
                        letterSpacing = 0.sp
                    )
                },
            )

            Tab(
                selected = selectedTabIndex == 1,
                selectedContentColor =
                if (isSystemInDarkTheme()) {
                    colorResource(id = R.color.white)
                } else colorResource(
                    id = R.color.black
                ),
                unselectedContentColor =
                if (isSystemInDarkTheme()) {
                    colorResource(id = R.color.white)
                } else colorResource(
                    id = R.color.black
                ),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.playlist),
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        fontSize = 14.sp,
                        letterSpacing = 0.sp
                    )
                },
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> FavoriteScreen(
                    state = stateFavorite,
                    onClick = onClickPlayer
                )

                1 -> PlaylistScreen(
                    state = statePlaylist,
                    onClickCurrentPlaylist = onClickCurrentPlaylist,
                    onClickNewPlaylist = onClickCreatePlaylist
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MediatekaScreenPreview() {
    val stateFavorite by remember {
        mutableStateOf(FavoriteState.Initial)
    }
    val statePlaylist by remember {
        mutableStateOf(PlaylistState.Initial)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        MediatekaScreen(
            stateFavorite = stateFavorite,
            statePlaylist = statePlaylist,
            onClickCreatePlaylist = { },
            onClickCurrentPlaylist = {}
        ) {

        }
    }
}