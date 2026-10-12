package com.verbpulse.app.feature.learning.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.verbpulse.app.feature.learning.data.GeminiRepository
import com.verbpulse.app.feature.learning.data.IrregularVerb
import com.verbpulse.app.feature.learning.data.VerbsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FlashcardUiState(
    val currentVerb: IrregularVerb? = null,
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val currentVerbList: List<IrregularVerb> = emptyList(),
    val contextualSentence: String = "",
    val isLoadingSentence: Boolean = false
)

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val verbsRepository: VerbsRepository,
    private val geminiRepository: GeminiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    private var currentCategory: String = "Tecnología"
    private var filteredVerbs: List<IrregularVerb> = emptyList()

    init {
        loadFirstVerb()
    }

    private fun loadFirstVerb() {
        val verb = if (filteredVerbs.isNotEmpty()) filteredVerbs[0] else verbsRepository.getVerbAt(0)
        verb?.let {
            val fallback = geminiRepository.getStaticFallback(it.present, currentCategory)
            _uiState.update { state ->
                state.copy(
                    currentVerb = it,
                    currentIndex = 0,
                    isFlipped = false,
                    currentVerbList = filteredVerbs.ifEmpty { verbsRepository.getAllVerbs() },
                    contextualSentence = fallback
                )
            }
            fetchDynamicSentence(it.present, currentCategory)
        }
    }

    fun loadVerbsByCategory(category: String) {
        currentCategory = category
        filteredVerbs = verbsRepository.getVerbsByCategory(category)
        loadFirstVerb()
    }

    private var sentenceJob: kotlinx.coroutines.Job? = null

    private fun fetchDynamicSentence(verb: String, category: String) {
        sentenceJob?.cancel()
        sentenceJob = viewModelScope.launch {
            val sentence = geminiRepository.getContextualSentence(verb, category)
            _uiState.update { it.copy(contextualSentence = sentence) }
        }
    }

    fun nextVerb() {
        val verbList = if (filteredVerbs.isNotEmpty()) filteredVerbs else verbsRepository.getAllVerbs()
        val nextIndex = _uiState.value.currentIndex + 1
        if (nextIndex < verbList.size) {
            val nextVerb = verbList[nextIndex]
            val fallback = geminiRepository.getStaticFallback(nextVerb.present, currentCategory)
            _uiState.update {
                it.copy(
                    currentVerb = nextVerb,
                    currentIndex = nextIndex,
                    isFlipped = false,
                    contextualSentence = fallback
                )
            }
            fetchDynamicSentence(nextVerb.present, currentCategory)
        } else {
            loadFirstVerb()
        }
    }

    fun previousVerb() {
        val verbList = if (filteredVerbs.isNotEmpty()) filteredVerbs else verbsRepository.getAllVerbs()
        val prevIndex = _uiState.value.currentIndex - 1
        if (prevIndex >= 0) {
            val prevVerb = verbList[prevIndex]
            val fallback = geminiRepository.getStaticFallback(prevVerb.present, currentCategory)
            _uiState.update {
                it.copy(
                    currentVerb = prevVerb,
                    currentIndex = prevIndex,
                    isFlipped = false,
                    contextualSentence = fallback
                )
            }
            fetchDynamicSentence(prevVerb.present, currentCategory)
        }
    }

    fun flip() {
        _uiState.update { it.copy(isFlipped = !it.isFlipped) }
    }
}
