package com.verbpulse.app.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.verbpulse.app.core.data.preferences.UserPreferencesRepository
import com.verbpulse.app.core.designsystem.VerbPulseTheme
import com.verbpulse.app.core.haptics.WearHapticsManager
import com.verbpulse.app.core.notification.VerbNotificationManager
import com.verbpulse.app.feature.learning.ui.FlashcardScreen
import com.verbpulse.app.feature.progress.data.ProgressRepository
import com.verbpulse.app.feature.progress.ui.ProgressScreen
import com.verbpulse.app.feature.quiz.ui.QuizScreen
import com.verbpulse.app.feature.reminders.domain.ReminderScheduler
import com.verbpulse.app.feature.setup.ui.HomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    @Inject
    lateinit var reminderScheduler: ReminderScheduler

    @Inject
    lateinit var hapticsManager: WearHapticsManager

    @Inject
    lateinit var progressRepository: ProgressRepository

    private var initialStartFromNotification by mutableStateOf(false)
    private var initialNotificationCategory by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        handleIntent(intent)

        setContent {
            VerbPulseApp(
                userPreferencesRepository = userPreferencesRepository,
                reminderScheduler = reminderScheduler,
                hapticsManager = hapticsManager,
                progressRepository = progressRepository,
                startFromNotification = initialStartFromNotification,
                notificationCategory = initialNotificationCategory
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return
        val fromNotification = intent.getBooleanExtra(VerbNotificationManager.EXTRA_START_LEARNING, false)
        if (fromNotification) {
            initialStartFromNotification = true
            initialNotificationCategory = intent.getStringExtra(VerbNotificationManager.EXTRA_CATEGORY)
        }
    }
}

@Composable
fun VerbPulseApp(
    userPreferencesRepository: UserPreferencesRepository,
    reminderScheduler: ReminderScheduler? = null,
    hapticsManager: WearHapticsManager? = null,
    progressRepository: ProgressRepository? = null,
    startFromNotification: Boolean = false,
    notificationCategory: String? = null
) {
    val context = LocalContext.current
    var showFlashcard by remember { mutableStateOf(startFromNotification) }
    var showQuiz by remember { mutableStateOf(false) }
    var showProgress by remember { mutableStateOf(false) }
    var isDarkMode by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf("ES") }
    var selectedCategory by remember { mutableStateOf(notificationCategory ?: "Tecnología") }
    val coroutineScope = rememberCoroutineScope()

    // Solicitar permiso de notificaciones en Wear OS 4+ (API 33+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                Timber.d("Permiso POST_NOTIFICATIONS en Wear OS: %b", isGranted)
            }
        )
        LaunchedEffect(Unit) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val userPreferences by userPreferencesRepository.userPreferences.collectAsStateWithLifecycle(
        initialValue = null
    )

    val currentStreak by (progressRepository?.currentStreak ?: kotlinx.coroutines.flow.flowOf(1))
        .collectAsStateWithLifecycle(initialValue = 1)

    userPreferences?.let { prefs ->
        isDarkMode = prefs.isDarkMode
        language = prefs.language
        if (!startFromNotification) {
            selectedCategory = prefs.selectedCategory
        }
    }

    VerbPulseTheme(darkTheme = isDarkMode) {
        when {
            showProgress -> {
                ProgressScreen(
                    onDone = {
                        hapticsManager?.playSubtleClick()
                        showProgress = false
                        showQuiz = false
                        showFlashcard = false
                    }
                )
            }
            showQuiz -> {
                QuizScreen(
                    selectedCategory = selectedCategory,
                    onBack = {
                        hapticsManager?.playSubtleClick()
                        showQuiz = false
                    },
                    onFinishQuiz = {
                        hapticsManager?.playSuccessPulse()
                        showQuiz = false
                        showProgress = true
                    }
                )
            }
            showFlashcard -> {
                FlashcardScreen(
                    selectedCategory = selectedCategory,
                    onBack = {
                        hapticsManager?.playSubtleClick()
                        showFlashcard = false
                    },
                    isDarkMode = isDarkMode,
                    language = language,
                    onThemeChange = {
                        hapticsManager?.playSubtleClick()
                        coroutineScope.launch {
                            userPreferencesRepository.updateIsDarkMode(!isDarkMode)
                        }
                    },
                    onLanguageChange = {
                        hapticsManager?.playSubtleClick()
                        coroutineScope.launch {
                            userPreferencesRepository.updateLanguage(if (language == "ES") "EN" else "ES")
                        }
                    },
                    onStartQuiz = {
                        hapticsManager?.playSubtleClick()
                        showFlashcard = false
                        showQuiz = true
                    }
                )
            }
            else -> {
                HomeScreen(
                    initialCategory = selectedCategory,
                    initialWords = (userPreferences?.dailyGoal ?: 10).toString(),
                    initialFrequency = "${userPreferences?.frequency ?: 1}h",
                    currentStreak = currentStreak,
                    onOpenProgress = {
                        hapticsManager?.playSubtleClick()
                        showProgress = true
                    },
                    onStartLearning = { category, words, frequency ->
                        hapticsManager?.playSubtleClick()
                        val goal = words.toIntOrNull() ?: 10
                        val freqHours = frequency.replace("h", "").toIntOrNull() ?: 1

                        coroutineScope.launch {
                            userPreferencesRepository.updateSelectedCategory(category)
                            userPreferencesRepository.updateDailyGoal(goal)
                            userPreferencesRepository.updateFrequency(freqHours)
                        }

                        // Programar recordatorios en segundo plano con WorkManager (Fase 5)
                        reminderScheduler?.schedulePeriodicReminders(freqHours)

                        selectedCategory = category
                        showFlashcard = true
                    },
                    isDarkMode = isDarkMode,
                    onThemeChange = {
                        hapticsManager?.playSubtleClick()
                        coroutineScope.launch {
                            userPreferencesRepository.updateIsDarkMode(!isDarkMode)
                        }
                    },
                    language = language,
                    onLanguageChange = {
                        hapticsManager?.playSubtleClick()
                        coroutineScope.launch {
                            userPreferencesRepository.updateLanguage(if (language == "ES") "EN" else "ES")
                        }
                    },
                    onStartQuiz = { category ->
                        hapticsManager?.playSubtleClick()
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
