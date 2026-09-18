package com.ecoloop.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF0596CE),
    onPrimary = Color(0xFF00355F),
    primaryContainer = Color(0xFFDBEAFF),
    secondary = Color(0xFF0083B0),
    onSecondary = Color(0xFF00355F),
    tertiary = Color(0xFF8564EC),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF1A1A1A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFE1E8EE),
    onSurfaceVariant = Color(0xFF454F58),
    outline = Color(0xFFB0B8C1),
    inversePrimary = Color(0xFF004D8C)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF4DA3E8),
    onPrimary = Color(0xFF00355F),
    primaryContainer = Color(0xFF004D8C),
    secondary = Color(0xFF2ED0E8),
    onSecondary = Color(0xFF00355F),
    tertiary = Color(0xFFC4A5FF),
    onTertiary = Color(0xFF000000),
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF181818),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2D3748),
    onSurfaceVariant = Color(0xFFA0A8B4),
    outline = Color(0xFF3E4C59),
    inversePrimary = Color(0xFFDBEAFF)
)

val ColorScheme.statusSuccess: Color
    get() = Color(0xFF1AA260)

val ColorScheme.statusWarning: Color
    get() = Color(0xFFF59E0B)

val ColorScheme.statusDanger: Color
    get() = Color(0xFFDC2626)

val ColorScheme.statusInfo: Color
    get() = Color(0xFF0EA5E9)

@Composable
fun EcoloopTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
