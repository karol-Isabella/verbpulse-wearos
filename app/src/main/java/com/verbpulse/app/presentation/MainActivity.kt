package com.verbpulse.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.verbpulse.app.core.designsystem.VerbPulseTheme
import com.verbpulse.app.feature.setup.ui.HomeScreen
import com.verbpulse.app.feature.learning.ui.FlashcardScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VerbPulseApp()
        }
    }
}

@Composable
fun VerbPulseApp() {
    val showFlashcard = remember { mutableStateOf(false) }
    VerbPulseTheme {
        if (showFlashcard.value) {
            FlashcardScreen(onBack = { showFlashcard.value = false })
        } else {
            HomeScreen(onStartLearning = { showFlashcard.value = true })
        }
    }
}

@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
fun DefaultPreview() {
    VerbPulseApp()
}
