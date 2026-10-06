package com.verbpulse.app.core.designsystem

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.ColorScheme

// Primary Colors
val OrangePrimary = Color(0xFFFF5216)
val OrangeLight = Color(0xFFFF8A50)
val OrangeDark = Color(0xFFC93A0E)

// Light Mode Colors
val White = Color(0xFFFFFFFF)
val SurfaceLight = Color(0xFFF2F2F7)
val OnSurfaceLight = Color(0xFF1C1C1E)
val MutedForegroundLight = Color(0xFF5C5C63)

// Dark Mode Colors
val Black = Color(0xFF000000)
val SurfaceDark = Color(0xFF1C1C1E)
val OnSurfaceDark = Color(0xFFEBEBF5)
val MutedForegroundDark = Color(0xFFA1A1A6)

// Semantic Colors
val ErrorRed = Color(0xFFB3261E)
val OnErrorLight = Color(0xFFFFFFFF)
val SuccessGreen = Color(0xFF34D399)
val DangerRed = Color(0xFFFF5C7A)

// Outline Colors
val OutlineLight = Color(0xFFC5C5CA)
val OutlineDark = Color(0xFF8E8E93)

// Light Theme ColorScheme
val LightColorScheme = ColorScheme(
    primary = OrangePrimary,
    primaryDim = OrangeDark,
    primaryContainer = OrangeLight,
    onPrimary = White,
    onPrimaryContainer = OrangeDark,
    secondary = OrangePrimary,
    secondaryDim = OrangeDark,
    secondaryContainer = SurfaceLight,
    onSecondary = White,
    onSecondaryContainer = OnSurfaceLight,
    tertiary = OrangePrimary,
    tertiaryDim = OrangeDark,
    tertiaryContainer = SurfaceLight,
    onTertiary = White,
    onTertiaryContainer = OnSurfaceLight,
    onSurface = OnSurfaceLight,
    onSurfaceVariant = MutedForegroundLight,
    outline = OutlineLight,
    outlineVariant = Color(0xFFE8E8ED),
    background = White,
    onBackground = OnSurfaceLight,
    error = ErrorRed,
    onError = OnErrorLight
)

// Dark Theme ColorScheme
val DarkColorScheme = ColorScheme(
    primary = OrangeLight,
    primaryDim = OrangeDark,
    primaryContainer = OrangeDark,
    onPrimary = Black,
    onPrimaryContainer = OrangeLight,
    secondary = OrangeDark,
    secondaryDim = Color(0xFF99411D),
    secondaryContainer = Color(0xFF3A3A3C),
    onSecondary = White,
    onSecondaryContainer = OnSurfaceDark,
    tertiary = OrangeDark,
    tertiaryDim = Color(0xFF99411D),
    tertiaryContainer = Color(0xFF3A3A3C),
    onTertiary = White,
    onTertiaryContainer = OnSurfaceDark,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = MutedForegroundDark,
    outline = OutlineDark,
    outlineVariant = Color(0xFF48484A),
    background = Black,
    onBackground = OnSurfaceDark,
    error = Color(0xFFF2B8B5),
    onError = OnErrorLight
)
