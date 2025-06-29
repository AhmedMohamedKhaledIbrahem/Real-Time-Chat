package com.example.realtimechatapp.ui.theme

import androidx.compose.ui.graphics.Color

// Greys - Light Mode
val blackLight = Color(0xFF000000) // Main text, active icon state
val greyLight = Color(0xFF7A7A7A) // Secondary text, default icon state
val greyGainsboroLight =
    Color(0xFFDBD8DB) // Disabled icons, empty states, app version text, self message background
val greyWhisperLight = Color(0xFFECEBEB) // Borders
val whiteSmokeLight = Color(0xFFF2F2F2) // Login text input background, section headings background
val whiteSnowLight = Color(0xFFFCFCFC) // App background
val whiteLight =
    Color(0xFFFFFFFF) // Button text, top bars background, bottom bars background, items background
val blueAliceLight = Color(0xFFE9F2FF) // Message link card background

// Specials - Light Mode
val bgGradientStartLight = Color(0xFFF7F7F7) // 1st section heading gradient start
val bgGradientEndLight = Color(0xFFFCFCFC) // 1st section heading gradient end
val overlayLight =
    Color(0x33000000) // 20% alpha black - long-press menu, my panel, channel action-sheet, image share, image text overlay
val overlayDarkLight =
    Color(0x99000000) // 60% alpha black - date stamp, Giphy inline label background
val buttonTextLight = Color(0xFFFFFFFF)
val buttonBackgroundLight = Color(0xFF005FFF)

// Effects - Light Mode (Base colors with intended alpha values)
val effectBorderTopLight = Color(0x14000000) // 8% alpha of black (for subtle shadow border)
val effectBorderBottomLight = Color(0x14000000) // 8% alpha of black
val effectShadowIconButtonLight =
    Color(0x40000000) // 25% alpha of black (new chat button, giphy arrow buttons)
val effectModalShadowLight =
    Color(0x99000000) // 60% alpha of black (instant commands modal, giphy modal)
val effectBackgroundBlurLight =
    Color(0xFF20E070) // Given in image as hex, but description "coupled with 'Overlay' style" suggests it might be a blur effect *with* an overlay. If it's a color, this is it.

// Accents - Light Mode
val accentBlueLight =
    Color(0xFF005FFF) // Primary color, selected icon state, CTA bg, white buttons label, links, instant commands
val accentRedLight =
    Color(0xFFFF3742) // Error text label, notification badge background, disruptive action text and icon
val accentRedLightContainer = Color(0xFFFFDAD6) // Default Material 3 light red error container
val accentRedLightText = Color(0xFF410002) // Default Material 3 dark text on error container
val accentGreenLight = Color(0xFF20E070) // Online status


// --- Dark Mode Colors ---

// Greys - Dark Mode
val blackDark = Color(0xFFFFFFFF) // Main text, active icon state
val greyDark = Color(0xFF7A7A7A) // Secondary text, default icon state
val greyGainsboroDark =
    Color(0xFF2D2F2F) // Disabled icons, empty states, app version text, self message background
val greyWhisperDark = Color(0xFF1C1E22) // Borders
val whiteSmokeDark = Color(0xFF13151B) // Login text input background, section headings background
val whiteSnowDark = Color(0xFF070A0D) // App background
val whiteDark =
    Color(0xFF101418) // Button text, Top bars background, Bottom bars background, Items background
val blueAliceDark = Color(0xFF00193D) // Message link card background

// Specials - Dark Mode
val bgGradientStartDark = Color(0xFF0A0C0F) // 1st section heading gradient start
val bgGradientEndDark = Color(0xFF070A0D) // 1st section heading gradient end
val overlayDark =
    Color(0x99000000) // 60% alpha black - long-press menu, my panel, channel action-sheet, image share, image text overlay
val overlayDarkDark =
    Color(0xCC000000) // 80% alpha black - date stamp, Giphy inline label background
val buttonTextDark = Color(0xFF005FFF) // Blue in dark mode
val buttonBackgroundDark = Color(0xFFFFFFFF) // White in dark mode

// Effects - Dark Mode (Base colors with intended alpha values)
val effectBorderTopDark = Color(0x14FFFFFF) // 8% alpha of white (for subtle light border)
val effectBorderBottomDark = Color(0x14FFFFFF) // 8% alpha of white
val effectShadowIconButtonDark =
    Color(0x80000000) // 50% alpha of black (new chat button, giphy arrow buttons)
val effectModalShadowDark =
    Color(0x80000000) // 50% alpha of black (instant commands modal, giphy modal)
val effectBackgroundBlurDark = Color(0xFF20E070) // Same hex, but context implies a blur effect.

// Accents - Dark Mode
val accentBlueDark =
    Color(0xFF005FFF) // Primary color, selected icon state, CTA bg, white buttons label, links, instant commands
val accentRedDark =
    Color(0xFFFF3742) // Error text label, notification badge background, disruptive action text and icon
val accentRedLightDarkContainer = Color(0xFF892B2B) // Default Material 3 light red error container
val accentRedLightTextDark = Color(0xFFFFDAD6) // Default Material 3 dark text on error container
val accentGreenDark = Color(0xFF20E070) // Online status