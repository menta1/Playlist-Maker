package com.example.playlistmaker.mediateka.favorite.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.model.Track

@Composable
fun FavoriteScreen(
    state: FavoriteState,
    onClick: (track: Track) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
    ) {
        when (state) {
            FavoriteState.Default -> {}

            FavoriteState.Empty -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 106.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.nothing_was_found),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(id = R.string.your_library_empty),
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        fontSize = 19.sp,
                        letterSpacing = 0.sp,
                        color = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                            id = R.color.black
                        )
                    )
                }

            }

            is FavoriteState.Success -> {
                LazyColumn {
                    items(state.tracks) { track ->
                        SongItem(
                            track = track,
                            onClick = onClick
                        )
                    }
                }
            }
        }
    }
}