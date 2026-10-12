package com.verbpulse.app.feature.quiz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.verbpulse.app.feature.learning.data.IrregularVerb
import com.verbpulse.app.feature.learning.data.VerbsRepository
import com.verbpulse.app.feature.progress.data.DailyProgressEntity
import com.verbpulse.app.feature.progress.data.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizUiState(
    val currentVerb: IrregularVerb? = null,
    val question: String = "",
    val options: List<String> = emptyList(),
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val score: Int = 0,
    val questionNumber: Int = 1,
    val totalQuestions: Int = 5,
    val isFinished: Boolean = false,
    val accuracyPercentage: Float = 0f,
    val currentStreak: Int = 0,
    val recordedProgress: DailyProgressEntity? = null
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val verbsRepository: VerbsRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var currentCategory: String = "Tecnología"
    private var categoryVerbs: List<IrregularVerb> = emptyList()

    init {
        categoryVerbs = verbsRepository.getAllVerbs()
        generateQuestion()
    }

    fun loadVerbsForCategory(category: String) {
        currentCategory = category
        categoryVerbs = verbsRepository.getVerbsByCategory(category)
        resetQuiz()
    }

    private fun generateQuestion() {
        val verbs = if (categoryVerbs.isNotEmpty()) categoryVerbs else verbsRepository.getAllVerbs()
        val verb = verbs.random()
        val correctAnswer = verb.past
        val wrongAnswers = verbsRepository.getRandomWrongAnswers(correctAnswer, 2)
        val allOptions = (listOf(correctAnswer) + wrongAnswers).shuffled()

        val question = "What is the past tense of ${verb.present}?"

        _uiState.update { state ->
            state.copy(
                currentVerb = verb,
                question = question,
                options = allOptions,
                selectedAnswer = null,
                isCorrect = null
            )
        }
    }

    fun selectAnswer(answer: String) {
        if (_uiState.value.selectedAnswer != null) return // Evitar doble respuesta

        val isCorrect = answer == _uiState.value.currentVerb?.past
        val newScore = if (isCorrect) _uiState.value.score + 1 else _uiState.value.score

        _uiState.update {
            it.copy(
                selectedAnswer = answer,
                isCorrect = isCorrect,
                score = newScore
            )
        }
    }

    fun nextQuestion() {
        val currentState = _uiState.value
        if (currentState.questionNumber < currentState.totalQuestions) {
            _uiState.update {
                it.copy(questionNumber = it.questionNumber + 1)
            }
            generateQuestion()
        } else {
            finishQuiz()
        }
    }

    fun finishQuiz() {
        val currentState = _uiState.value
        val total = currentState.totalQuestions
        val correct = currentState.score
        val accuracy = if (total > 0) (correct.toFloat() / total) * 100f else 0f

        viewModelScope.launch {
            val progressEntity = progressRepository.recordQuizResult(
                category = currentCategory,
                totalQuestions = total,
                correctAnswers = correct,
                wordsLearned = total
            )

            _uiState.update {
                it.copy(
                    isFinished = true,
                    accuracyPercentage = accuracy,
                    currentStreak = progressEntity.streakDays,
                    recordedProgress = progressEntity
                )
            }
        }
    }

    fun resetQuiz() {
        _uiState.value = QuizUiState(totalQuestions = 5)
        generateQuestion()
    }
}
