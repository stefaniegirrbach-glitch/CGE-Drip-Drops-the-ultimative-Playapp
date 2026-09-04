package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CgeDarkColorScheme = darkColorScheme(
    primary = CgeGold,
    secondary = CgeAccentPink,
    tertiary = CgeCyan,
    background = CgeDeepPurple,
    surface = CgeSurfacePurple,
    onPrimary = CgeDarkPurple,
    onSecondary = CgeWhite,
    onBackground = CgeWhite,
    onSurface = CgeWhite
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = CgeDarkColorScheme,
        typography = Typography,
        content = content
    )
}
