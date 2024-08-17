package com.example.playlistmaker.mediateka.playlist.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import java.io.File

@Composable
fun PlaylistItem(
    playlist: Playlist,
    onClick: (playlist: Playlist) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick(playlist) }
            .padding(vertical = 16.dp)
    ) {

        AsyncImage(
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(8.dp)),
            model = File(playlist.filePath),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = playlist.title,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            fontSize = 12.sp,
            letterSpacing = 0.sp,
            color = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                id = R.color.black
            )
        )
        Text(
            text = pluralStringResource(
                id = R.plurals.tracks,
                count = playlist.countTracks,
                playlist.countTracks
            ),
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            fontSize = 12.sp,
            letterSpacing = 0.sp,
            color = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                id = R.color.black
            )
        )
    }
}