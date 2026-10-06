package com.verbpulse.app.feature.quiz.ui

import androidx.lifecycle.ViewModel
import com.verbpulse.app.feature.learning.data.IrregularVerb
import com.verbpulse.app.feature.learning.data.VerbsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class QuizUiState(
    val currentVerb: IrregularVerb? = null,
    val question: String = "",
    val options: List<String> = emptyList(),
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val score: Int = 0,
    val questionNumber: Int = 1
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val verbsRepository: VerbsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState

    init {
        generateQuestion()
    }

    private fun generateQuestion() {
        val verb = verbsRepository.getRandomVerb()
        val correctAnswer = verb.past
        val wrongAnswers = verbsRepository.getRandomWrongAnswers(correctAnswer, 2)
        val allOptions = (listOf(correctAnswer) + wrongAnswers).shuffled()

        val question = "What is the past tense of ${verb.present}?"

        _uiState.value = QuizUiState(
            currentVerb = verb,
            question = question,
            options = allOptions,
            selectedAnswer = null,
            isCorrect = null,
            score = _uiState.value.score,
            questionNumber = _uiState.value.questionNumber
        )
    }

    fun selectAnswer(answer: String) {
        val isCorrect = answer == _uiState.value.currentVerb?.past
        _uiState.value = _uiState.value.copy(
            selectedAnswer = answer,
            isCorrect = isCorrect,
            score = if (isCorrect) _uiState.value.score + 1 else _uiState.value.score
        )
    }

    fun nextQuestion() {
        generateQuestion()
        _uiState.value = _uiState.value.copy(
            questionNumber = _uiState.value.questionNumber + 1
        )
    }

    fun resetQuiz() {
        _uiState.value = QuizUiState()
        generateQuestion()
    }
}
