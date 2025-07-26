package com.example.realtimechatapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp


private val LightColorScheme = lightColorScheme(
    primary = accentBlueLight, // Main brand color
    onPrimary = buttonTextLight, // Text/icons on primary color
    primaryContainer = blueAliceLight, // A lighter version of primary, or a color used for containers related to primary
    onPrimaryContainer = blackLight, // Text/icons on primaryContainer

    secondary = accentGreenLight, // Secondary brand color (online status, etc.)
    onSecondary = buttonTextLight, // Text/icons on secondary color
    secondaryContainer = whiteSmokeLight, // Containers related to secondary
    onSecondaryContainer = blackLight,

    tertiary = accentRedLight, // Tertiary brand color (error, notifications)
    onTertiary = buttonTextLight, // Text/icons on tertiary color
    tertiaryContainer = greyGainsboroLight, // Containers related to tertiary
    onTertiaryContainer = blackLight,

    background = whiteSnowLight, // App background
    onBackground = blackLight, // Main text color on background

    surface = whiteLight, // Card, dialog, sheet background
    onSurface = blackLight, // Main text color on surface
    surfaceVariant = greyWhisperLight, // Borders, dividers, subtle surface variants
    onSurfaceVariant = greyLight, // Secondary text on surface variants

    error = accentRedLight,
    onError = buttonTextLight, // White text on error
    errorContainer = accentRedLightContainer, // Default Material 3 light red error container
    onErrorContainer = accentRedLightText, // Default Material 3 dark text on error container

    outline = greyWhisperLight, // General borders/outlines
)

private val DarkColorScheme = darkColorScheme(
    primary = accentBlueDark,
    onPrimary = buttonTextDark, // Blue text on primary in dark mode
    primaryContainer = blueAliceDark,
    onPrimaryContainer = blackDark, // White text on primaryContainer in dark mode

    secondary = accentGreenDark,
    onSecondary = blackDark, // White text on secondary in dark mode (assuming white text on green)
    secondaryContainer = whiteSmokeDark,
    onSecondaryContainer = blackDark,

    tertiary = accentRedDark,
    onTertiary = blackDark, // White text on tertiary in dark mode
    tertiaryContainer = greyGainsboroDark,
    onTertiaryContainer = blackDark,

    background = whiteSnowDark, // App background
    onBackground = blackDark, // Main text color on background

    surface = whiteDark, // Card, dialog, sheet background
    onSurface = blackDark, // Main text color on surface
    surfaceVariant = greyWhisperDark, // Borders, dividers, subtle surface variants
    onSurfaceVariant = greyDark, // Secondary text on surface variants

    error = accentRedDark,
    onError = blackDark, // White text on error
    errorContainer = accentRedLightDarkContainer, // Default Material 3 dark red error container
    onErrorContainer = accentRedLightTextDark, // Default Material 3 light text on error container

    outline = greyWhisperDark,
)
val shapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(18.dp),
)

@Composable
fun RealTimeChatAppTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}