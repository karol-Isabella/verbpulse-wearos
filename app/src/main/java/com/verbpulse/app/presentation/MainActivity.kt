package com.verbpulse.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.verbpulse.app.core.designsystem.VerbPulseTheme
import com.verbpulse.app.feature.learning.ui.FlashcardScreen
import com.verbpulse.app.feature.setup.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { VerbPulseApp() }
    }
}

@Composable
fun VerbPulseApp() {
    var showFlashcard by remember { mutableStateOf(false) }
    var isDarkMode by remember { mutableStateOf(true) }
    var language by remember { mutableStateOf("ES") }

    VerbPulseTheme(darkTheme = isDarkMode) {
        if (showFlashcard) {
            FlashcardScreen(onBack = { showFlashcard = false })
        } else {
            HomeScreen(
                onStartLearning = { showFlashcard = true },
                isDarkMode = isDarkMode,
                onThemeChange = { isDarkMode = !isDarkMode },
                language = language,
                onLanguageChange = { language = if (language == "ES") "EN" else "ES" }
            )
        }
    }
}

@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
fun DefaultPreview() {
    VerbPulseApp()
}