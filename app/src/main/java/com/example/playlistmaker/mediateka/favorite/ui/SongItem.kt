package com.example.playlistmaker.mediateka.favorite.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SongItem(
    track: Track,
    onClick: (track: Track) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(track) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {

            AsyncImage(
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(2.dp)),
                model = track.artworkUrl60, contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.padding(top = 6.dp)
            ) {
                Text(
                    text = track.trackName ?: "",
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    fontSize = 16.sp,
                    letterSpacing = 0.sp,
                    color = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                        id = R.color.black
                    )
                )
                Row(
                    modifier = Modifier.padding(top = 1.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = track.artistName ?: "",
                        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                        fontSize = 11.sp,
                        letterSpacing = 0.sp,
                        color = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                            id = R.color.grey
                        )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ellipse),
                        contentDescription = null,
                        tint = colorResource(id = R.color.grey)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = (SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(track.trackTimeMillis)).toString(),
                        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                        fontSize = 11.sp,
                        letterSpacing = 0.sp,
                        color = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(
                            id = R.color.grey
                        )
                    )
                }
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.arrow_forward_24),
            contentDescription = null,
            tint = colorResource(
                id = R.color.grey
            )
        )
    }
}