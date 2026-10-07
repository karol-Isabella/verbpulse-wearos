package com.verbpulse.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.verbpulse.app.core.data.preferences.UserPreferencesRepository
import com.verbpulse.app.core.designsystem.VerbPulseTheme
import com.verbpulse.app.feature.learning.ui.FlashcardScreen
import com.verbpulse.app.feature.quiz.ui.QuizScreen
import com.verbpulse.app.feature.setup.ui.HomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { VerbPulseApp(userPreferencesRepository) }
    }
}

@Composable
fun VerbPulseApp(userPreferencesRepository: UserPreferencesRepository) {
    var showFlashcard by remember { mutableStateOf(false) }
    var showQuiz by remember { mutableStateOf(false) }
    var isDarkMode by remember { mutableStateOf(true) }
    var language by remember { mutableStateOf("ES") }
    var selectedCategory by remember { mutableStateOf("Tecnología") }
    val coroutineScope = rememberCoroutineScope()

    val userPreferences by userPreferencesRepository.userPreferences.collectAsStateWithLifecycle(
        initialValue = null
    )

    userPreferences?.let { prefs ->
        isDarkMode = prefs.isDarkMode
        language = prefs.language
        selectedCategory = prefs.selectedCategory
    }

    VerbPulseTheme(darkTheme = isDarkMode) {
        when {
            showQuiz -> {
                QuizScreen(
                    selectedCategory = selectedCategory,
                    onBack = { showQuiz = false }
                )
            }
            showFlashcard -> {
                FlashcardScreen(
                    selectedCategory = selectedCategory,
                    onBack = { showFlashcard = false },
                    isDarkMode = isDarkMode,
                    language = language,
                    onThemeChange = {
                        coroutineScope.launch {
                            userPreferencesRepository.updateIsDarkMode(!isDarkMode)
                        }
                    },
                    onLanguageChange = {
                        coroutineScope.launch {
                            userPreferencesRepository.updateLanguage(if (language == "ES") "EN" else "ES")
                        }
                    },
                    onStartQuiz = { showQuiz = true }
                )
            }
            else -> {
                HomeScreen(
                    onStartLearning = { category ->
                        coroutineScope.launch {
                            userPreferencesRepository.updateSelectedCategory(category)
                        }
                        selectedCategory = category
                        showFlashcard = true
                    },
                    isDarkMode = isDarkMode,
                    onThemeChange = {
                        coroutineScope.launch {
                            userPreferencesRepository.updateIsDarkMode(!isDarkMode)
                        }
                    },
                    language = language,
                    onLanguageChange = {
                        coroutineScope.launch {
                            userPreferencesRepository.updateLanguage(if (language == "ES") "EN" else "ES")
                        }
                    },
                    onStartQuiz = { category ->
                        coroutineScope.launch {
                            userPreferencesRepository.updateSelectedCategory(category)
                        }
                        selectedCategory = category
                        showQuiz = true
                    }
                )
            }
        }
    }
}

@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
fun DefaultPreview() {
    VerbPulseTheme {
        HomeScreen()
    }
}
