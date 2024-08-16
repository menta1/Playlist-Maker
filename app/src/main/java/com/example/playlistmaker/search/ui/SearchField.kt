package com.example.playlistmaker.search.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onClearField: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
    ) {

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = colorResource(id = R.color.light_grey)),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.padding(start = 12.dp),
                painter = painterResource(id = R.drawable.search),
                contentDescription = null,
                tint = if (isSystemInDarkTheme()) colorResource(id = R.color.black) else colorResource(
                    id = R.color.grey
                )
            )

            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = 8.dp,
                        vertical = 8.dp),
                value = value,
                onValueChange = onValueChange,
                interactionSource = interactionSource,
                cursorBrush = SolidColor(colorResource(id = R.color.blue)),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.search),
                            maxLines = 1,
                            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                            fontSize = 16.sp,
                            color = if (isSystemInDarkTheme()) colorResource(id = R.color.black) else colorResource(
                                id = R.color.grey
                            ),
                            letterSpacing = 0.sp
                        )
                    }
                    innerTextField()
                },
                textStyle = TextStyle(
                    fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.black),
                    letterSpacing = 0.sp
                ),
                singleLine = true,
            )

            if (value.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clickable { onClearField() },
                    painter = painterResource(id = R.drawable.clear_icon),
                    contentDescription = null,
                    tint = if (isSystemInDarkTheme()) colorResource(id = R.color.black) else colorResource(
                        id = R.color.grey
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TextInputPreviewNight() {
    Column(modifier = Modifier.fillMaxSize()) {
        var text by remember { mutableStateOf("") }
        SearchField(value = text, onValueChange = {
            text = it
        }, modifier = Modifier.padding(45.dp), onClearField = {})
    }
}

@Preview(showBackground = true)
@Composable
fun TextInputPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        var text by remember { mutableStateOf("") }
        SearchField(value = text, onValueChange = {
            text = it
        }, modifier = Modifier.padding(45.dp), onClearField = {})
    }
}