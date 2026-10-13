package com.verbpulse.app.core.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text

@Composable
fun VerbPulseTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = VerbPulseTypography,
        shapes = VerbPulseShapes,
        content = content
    )
}

@Preview(name = "Round", widthDp = 227, heightDp = 227)
@Composable
private fun VerbPulseThemePreviewRound() {
    VerbPulseTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "VerbPulse",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}

@Preview(name = "Square", widthDp = 220, heightDp = 220)
@Composable
private fun VerbPulseThemePreviewSquare() {
    VerbPulseTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "VerbPulse",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}
