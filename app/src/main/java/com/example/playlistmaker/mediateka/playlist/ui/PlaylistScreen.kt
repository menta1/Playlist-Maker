package com.example.playlistmaker.mediateka.playlist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.Playlist

@Composable
fun PlaylistScreen(
    state: PlaylistState,
    onClickCurrentPlaylist: (playlist: Playlist) -> Unit,
    onClickNewPlaylist: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = onClickNewPlaylist, colors = ButtonColors(
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
                text = stringResource(id = R.string.new_playlist),
                fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                fontSize = 14.sp,
                letterSpacing = 0.sp,
                color = if (isSystemInDarkTheme()) colorResource(id = R.color.black) else colorResource(
                    id = R.color.white
                ),
            )
        }

        when (state) {
            PlaylistState.Default -> {

            }

            PlaylistState.Empty -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 46.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.nothing_was_found),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(id = R.string.not_created_playlist),
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        fontSize = 19.sp,
                        letterSpacing = 0.sp,
                        color = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                            id = R.color.black
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            is PlaylistState.Success -> {

                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(2)
                ) {
                    items(state.playlist) { playlist ->
                        PlaylistItem(playlist = playlist, onClick = onClickCurrentPlaylist)
                    }
                }
            }
        }
    }
}