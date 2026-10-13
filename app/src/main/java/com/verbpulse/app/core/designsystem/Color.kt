package com.verbpulse.app.core.designsystem

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.ColorScheme

// Primary Flame Orange Colors (Apple Watch Style - Flame #E34A27)
val OrangeFlame = Color(0xFFE34A27)
val OrangePrimary = OrangeFlame
val OrangeCoral = OrangeFlame
val OrangeLight = Color(0xFFFF6E40)
val OrangeDark = Color(0xFFBA2D0F)

// Light Mode Colors
val White = Color(0xFFFFFFFF)
val SurfaceLight = Color(0xFFF0F0F3)
val OnSurfaceLight = Color(0xFF0A0A0A)
val MutedForegroundLight = Color(0xFF5C5C63)

// Dark Mode Colors
val Black = Color(0xFF000000)
val SurfaceDark = Color(0xFF1C1C1E)
val OnSurfaceDark = Color(0xFFFFFFFF)
val MutedForegroundDark = Color(0xFFA1A1A6)

// Semantic Colors
val ErrorRed = Color(0xFFD32F2F)
val OnErrorLight = Color(0xFFFFFFFF)
val SuccessGreen = Color(0xFF28A745)
val RedVibrantDark = Color(0xFFFF3B30)
val RedVibrantLight = Color(0xFFD32F2F)
val DangerRed = RedVibrantDark

// Outline Colors
val OutlineLight = Color(0xFFE0E0E5)
val OutlineDark = Color(0xFF38383A)

// Light Theme ColorScheme
val LightColorScheme = ColorScheme(
    primary = OrangePrimary,
    primaryDim = OrangeDark,
    primaryContainer = OrangeLight,
    onPrimary = White,
    onPrimaryContainer = OnSurfaceLight,
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
    primary = OrangeCoral,
    primaryDim = OrangeDark,
    primaryContainer = Color(0xFF2C2C2E),
    onPrimary = Black,
    onPrimaryContainer = White,
    secondary = OrangeCoral,
    secondaryDim = OrangeDark,
    secondaryContainer = SurfaceDark,
    onSecondary = Black,
    onSecondaryContainer = White,
    tertiary = OrangeCoral,
    tertiaryDim = OrangeDark,
    tertiaryContainer = SurfaceDark,
    onTertiary = Black,
    onTertiaryContainer = White,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = MutedForegroundDark,
    outline = OutlineDark,
    outlineVariant = Color(0xFF2C2C2E),
    background = Black,
    onBackground = OnSurfaceDark,
    error = DangerRed,
    onError = White
)
