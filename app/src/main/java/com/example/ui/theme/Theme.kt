package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SpotCheckDarkScheme = darkColorScheme(
    primary = DetectiveViolet,
    onPrimary = Color.White,
    secondary = DetectiveCyan,
    onSecondary = Color.Black,
    tertiary = VerifiedEmerald,
    background = Slate950,
    onBackground = TextPrimary,
    surface = Slate900,
    onSurface = TextPrimary,
    surfaceVariant = Slate800,
    onSurfaceVariant = TextSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = SpotCheckDarkScheme,
        typography = Typography,
        content = content
    )
}
