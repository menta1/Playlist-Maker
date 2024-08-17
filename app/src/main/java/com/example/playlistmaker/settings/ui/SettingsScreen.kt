package com.example.playlistmaker.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Composable
fun SettingsScreen(
    onEvent: (event: SettingsEvent) -> Unit,
    state: Boolean
) {
    val link = stringResource(id = R.string.share_app_link)
    val email = stringResource(R.string.extra_text_write_email)
    val text = stringResource(R.string.extra_text_write_support)
    val subject = stringResource(R.string.extra_text_write_subject)
    val terms = stringResource(R.string.terms_use_link)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 14.dp)
    ) {

        Text(
            text = stringResource(id = R.string.settings),
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            fontSize = 22.sp,
            color = if (state) colorResource(id = R.color.white) else colorResource(id = R.color.black),
            letterSpacing = 0.sp
        )

        Spacer(modifier = Modifier.height(61.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.black),
                fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                fontSize = 16.sp,
                color = if (state) colorResource(id = R.color.white) else colorResource(id = R.color.black),
                letterSpacing = 0.sp
            )

            Switch(
                checked = state,
                onCheckedChange = { onEvent(SettingsEvent.SwitchTheme) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(id = R.color.grey),
                    checkedTrackColor = colorResource(id = R.color.light_grey),
                    uncheckedThumbColor = colorResource(id = R.color.grey),
                    uncheckedTrackColor = colorResource(id = R.color.light_grey)
                )
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        ItemSettings(text = R.string.share_app, icon = R.drawable.share, state = state) {
            onEvent(SettingsEvent.ShareAppButton(link))
        }

        Spacer(modifier = Modifier.height(42.dp))
        ItemSettings(text = R.string.help, icon = R.drawable.support, state = state) {
            onEvent(
                SettingsEvent.WriteSupport(
                    email = email,
                    text = text,
                    subject = subject
                )
            )
        }

        Spacer(modifier = Modifier.height(42.dp))
        ItemSettings(text = R.string.terms, icon = R.drawable.arrow_forward, state = state) {
            onEvent(SettingsEvent.TermsUseButton(terms))
        }

    }
}

@Composable
private fun ItemSettings(text: Int, icon: Int, state: Boolean, onClick: () -> Unit) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = text),
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            fontSize = 16.sp,
            color = if (state) colorResource(id = R.color.white) else colorResource(id = R.color.black),
            letterSpacing = 0.sp
        )

        Icon(
            modifier = Modifier.clickable { onClick() },
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = colorResource(id = R.color.grey)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSettingsScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        SettingsScreen(onEvent = {}, state = false)
    }
}