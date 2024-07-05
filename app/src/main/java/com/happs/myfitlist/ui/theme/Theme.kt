package com.happs.myfitlist.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

private val LightColors = lightColorScheme(
    primary = light_background,
    secondary = light_text,
    onSecondary = light_bg_itens, //cor para backgrounds de itens
    tertiary = light_bg_text, //cor para alguns textos
    onTertiary = light_title
)

private val DarkColors = darkColorScheme(
    primary = dark_background,
    secondary = dark_text,
    onSecondary = dark_bg_itens, //cor para backgrounds de itens
    tertiary = dark_bg_text, //cor para alguns textos
    onTertiary = dark_title
)

@Composable
fun ThemeSwitcherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> {
            DarkColors
        }

        else -> {
            LightColors
        }
    }

    (LocalView.current.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
    (LocalView.current.context as Activity).window.navigationBarColor = colorScheme.primary.toArgb()

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}