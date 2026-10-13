package com.verbpulse.app.feature.learning.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.verbpulse.app.feature.learning.data.GeminiRepository
import com.verbpulse.app.feature.learning.data.IrregularVerb
import com.verbpulse.app.feature.learning.data.VerbsRepository
import com.verbpulse.app.feature.progress.data.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FlashcardUiState(
    val currentVerb: IrregularVerb? = null,
    val isFlipped: Boolean = false,
    val contextualSentence: String = "",
    val isLoadingSentence: Boolean = false
)

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val verbsRepository: VerbsRepository,
    private val geminiRepository: GeminiRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    private var currentCategory: String = "Tecnología"
    private var filteredVerbs: List<IrregularVerb> = emptyList()

    init {
        filteredVerbs = verbsRepository.getVerbsByCategory(currentCategory)
        loadFirstVerb()
    }

    private fun loadFirstVerb() {
        if (filteredVerbs.isEmpty()) {
            filteredVerbs = verbsRepository.getVerbsByCategory(currentCategory)
        }
        val verb = filteredVerbs.randomOrNull() ?: verbsRepository.getVerbAt(0)
        verb?.let {
            val fallback = geminiRepository.getStaticFallback(it.present, currentCategory)
            _uiState.update { state ->
                state.copy(
                    currentVerb = it,
                    isFlipped = false,
                    contextualSentence = fallback
                )
            }
            fetchDynamicSentence(it.present, currentCategory)
        }
    }

    fun onGotIt(onComplete: () -> Unit) {
        viewModelScope.launch {
            progressRepository.recordWordLearned(currentCategory)
            onComplete()
        }
    }

    fun loadVerbsByCategory(category: String) {
        currentCategory = category
        filteredVerbs = verbsRepository.getVerbsByCategory(category)
        loadFirstVerb()
    }

    private var sentenceJob: Job? = null

    private fun fetchDynamicSentence(verb: String, category: String) {
        sentenceJob?.cancel()
        sentenceJob = viewModelScope.launch {
            val sentence = geminiRepository.getContextualSentence(verb, category)
            _uiState.update { it.copy(contextualSentence = sentence) }
        }
    }

    fun flip() {
        _uiState.update { it.copy(isFlipped = !it.isFlipped) }
    }
}
