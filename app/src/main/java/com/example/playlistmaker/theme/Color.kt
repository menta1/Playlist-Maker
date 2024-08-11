package com.example.playlistmaker.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val blue = Color(0xFF3772E7)
val purple_700 = Color(0xFF3700B3)
val teal_200 = Color(0xFF3772E7)
val teal_700 = Color(0xFF018786)
val black = Color(0xFF1A1B22)
val white = Color(0xFFFFFFFF)
val grey = Color(0xFFAEAFB4)
val light_grey = Color(0xFFE6E8EB)

data class LightColors(
    val primary: Color = blue,
    val primaryVariant: Color = purple_700,
    val onPrimary: Color = white,
    val secondary: Color = grey,
    val secondaryVariant: Color = grey,
    val onSecondary: Color = black,
    val windowBackground: Color = white,
    val statusBarColor: Color = white,

)

data class DarkColors(
    val primary: Color = blue,
    val primaryVariant: Color = purple_700,
    val onPrimary: Color = black,
    val secondary: Color = teal_200,
    val secondaryVariant: Color = white,
    val onSecondary: Color = white,
    val windowBackground: Color = blue,
)
