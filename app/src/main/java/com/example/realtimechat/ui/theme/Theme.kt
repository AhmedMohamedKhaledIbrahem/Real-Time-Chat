package com.example.realtimechat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp


private val LightColorScheme = lightColorScheme(
    primary = primaryLight, // Main brand color
    onPrimary = white, // Text/icons on primary color
    primaryContainer = primaryContainerLight, // A lighter version of primary, or a color used for containers related to primary
    onPrimaryContainer = blackLight, // Text/icons on primaryContainer

    secondary = accentGreenLight, // Secondary brand color (online status, etc.)
    onSecondary = white, // Text/icons on secondary color
    secondaryContainer = secondaryContainerLight, // Containers related to secondary
    onSecondaryContainer = blackLight,

    tertiary = accentRedLight, // Tertiary brand color (error, notifications)
    onTertiary = white, // Text/icons on tertiary color
    tertiaryContainer = tertiaryContainerLight, // Containers related to tertiary
    onTertiaryContainer = blackLight,

    background = backgroundLight, // App background
    onBackground = blackLight, // Main text color on background

    surface = surfaceLight, // Card, dialog, sheet background
    onSurface = blackLight, // Main text color on surface
    surfaceVariant = greyWhisperLight, // Borders, dividers, subtle surface variants
    onSurfaceVariant = onSurfaceVariantLight, // Secondary text on surface variants

    error = accentRedLight,
    onError = white, // White text on error
    errorContainer = accentRedLightContainer, // Default Material 3 light red error container
    onErrorContainer = onErrorContainerText, // Default Material 3 dark text on error container

    outline = greyWhisperLight, // General borders/outlines
)

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark, // Blue text on primary in dark mode
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = white, // White text on primaryContainer in dark mode

    secondary = secondaryDark,
    onSecondary = white, // White text on secondary in dark mode (assuming white text on green)
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = white,

    tertiary = accentRedDark,
    onTertiary = white, // White text on tertiary in dark mode
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = white,

    background = backgroundDark, // App background
    onBackground = white, // Main text color on background

    surface = whiteDark, // Card, dialog, sheet background
    onSurface = white, // Main text color on surface
    surfaceVariant = greyWhisperDark, // Borders, dividers, subtle surface variants
    onSurfaceVariant = onSurfaceVariantDark, // Secondary text on surface variants

    error = accentRedDark,
    onError = white, // White text on error
    errorContainer = errorContainerDark, // Default Material 3 dark red error container
    onErrorContainer = onErrorContainerDark, // Default Material 3 light text on error container

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