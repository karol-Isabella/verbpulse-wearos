package com.verbpulse.app.feature.progress.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.verbpulse.app.feature.progress.data.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ProgressUiState(
    val latestScorePercentage: Float = 0f,
    val streakDays: Int = 0,
    val totalWordsLearned: Int = 0,
    val averageScorePercentage: Float = 0f,
    val latestCategory: String = "Tecnología",
    val correctAnswers: Int = 0,
    val totalQuestions: Int = 5
)

@HiltViewModel
class ProgressViewModel @Inject constructor(
    progressRepository: ProgressRepository
) : ViewModel() {

    val uiState: StateFlow<ProgressUiState> = combine(
        progressRepository.latestProgress,
        progressRepository.currentStreak,
        progressRepository.totalWordsLearned,
        progressRepository.averageScorePercentage
    ) { latest, streak, words, avgScore ->
        ProgressUiState(
            latestScorePercentage = latest?.scorePercentage ?: 100f,
            streakDays = streak.coerceAtLeast(latest?.streakDays ?: 1),
            totalWordsLearned = words.coerceAtLeast(latest?.wordsLearned ?: 10),
            averageScorePercentage = if (avgScore > 0f) avgScore else (latest?.scorePercentage ?: 100f),
            latestCategory = latest?.category ?: "Tecnología",
            correctAnswers = latest?.correctAnswers ?: 5,
            totalQuestions = latest?.totalQuestions ?: 5
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProgressUiState()
    )
}
