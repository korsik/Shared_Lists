package com.korsik.exp.sharedlists.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
        primary = primaryColor,
        primaryVariant = primaryColorDark,
        secondary = accentColor,

        background = primaryTextColor,
        surface = primaryTextColor,
        onPrimary = primaryTextColor,
        onSecondary = dividerColor,
        onBackground = textIconsColor,
        onSurface = textIconsColor,
)

private val LightColorPalette = lightColors(
        primary = primaryColor,
        primaryVariant = primaryColorLight,
        secondary = accentColor,

//        /* Other default colors to override
        background = textIconsColor,
        surface = textIconsColor,
        onPrimary = textIconsColor,
        onSecondary = secondaryTextColor,
        onBackground = primaryTextColor,
        onSurface = primaryTextColor,
//    */
)

@Composable
fun SharedListsTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}