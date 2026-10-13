package com.verbpulse.app.feature.quiz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.verbpulse.app.feature.learning.data.IrregularVerb
import com.verbpulse.app.feature.learning.data.VerbsRepository
import com.verbpulse.app.feature.progress.data.DailyProgressEntity
import com.verbpulse.app.feature.progress.data.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    val correctAnswer: String = "",
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
        categoryVerbs = verbsRepository.getVerbsByCategory(currentCategory)
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

        val question: String
        val correctAnswer: String
        val allOptions: List<String>

        if (verb.isPhrasal) {
            // Pregunta estilo: "Figure out" means...
            val verbCapitalized = verb.present.split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
            question = "\"$verbCapitalized\" means…"
            correctAnswer = verb.translationEs.split(",").first().trim().replaceFirstChar { it.uppercase() }
            val wrongAnswers = verbsRepository.getRandomWrongMeanings(correctAnswer, 2)
            allOptions = (listOf(correctAnswer) + wrongAnswers).shuffled()
        } else {
            // Pregunta estilo: Past tense of "build"
            question = "Past tense of \"${verb.present}\""
            correctAnswer = verb.past.replaceFirstChar { it.uppercase() }
            val wrongAnswers = verbsRepository.getRandomWrongAnswers(verb.past, 2)
                .map { it.replaceFirstChar { c -> c.uppercase() } }
            allOptions = (listOf(correctAnswer) + wrongAnswers).shuffled()
        }

        _uiState.update { state ->
            state.copy(
                currentVerb = verb,
                question = question,
                correctAnswer = correctAnswer,
                options = allOptions,
                selectedAnswer = null,
                isCorrect = null
            )
        }
    }

    fun selectAnswer(answer: String) {
        if (_uiState.value.selectedAnswer != null) return // Evitar selecciones múltiples

        val correctAnswer = _uiState.value.correctAnswer
        val isCorrect = answer.equals(correctAnswer, ignoreCase = true)
        val newScore = if (isCorrect) _uiState.value.score + 1 else _uiState.value.score

        _uiState.update {
            it.copy(
                selectedAnswer = answer,
                isCorrect = isCorrect,
                score = newScore
            )
        }

        // Delay de retroalimentación de 900ms y avance automático (estilo Apple Watch)
        viewModelScope.launch {
            delay(900)
            if (_uiState.value.questionNumber < _uiState.value.totalQuestions) {
                _uiState.update {
                    it.copy(questionNumber = it.questionNumber + 1)
                }
                generateQuestion()
            } else {
                finishQuiz()
            }
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
