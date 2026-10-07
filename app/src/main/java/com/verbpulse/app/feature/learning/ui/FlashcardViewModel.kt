package com.verbpulse.app.feature.learning.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.verbpulse.app.feature.learning.data.IrregularVerb
import com.verbpulse.app.feature.learning.data.VerbsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class FlashcardUiState(
    val currentVerb: IrregularVerb? = null,
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val currentVerbList: List<IrregularVerb> = emptyList()
)

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val verbsRepository: VerbsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState
    private var currentCategory: String = "Tecnología"
    private var filteredVerbs: List<IrregularVerb> = emptyList()

    init {
        loadFirstVerb()
    }

    private fun loadFirstVerb() {
        val verb = if (filteredVerbs.isNotEmpty()) filteredVerbs[0] else verbsRepository.getVerbAt(0)
        _uiState.value = _uiState.value.copy(
            currentVerb = verb,
            currentIndex = 0,
            currentVerbList = filteredVerbs.ifEmpty { verbsRepository.getAllVerbs() }
        )
    }

    fun loadVerbsByCategory(category: String) {
        currentCategory = category
        filteredVerbs = verbsRepository.getVerbsByCategory(category)
        loadFirstVerb()
    }

    fun nextVerb() {
        val verbList = if (filteredVerbs.isNotEmpty()) filteredVerbs else verbsRepository.getAllVerbs()
        val nextIndex = _uiState.value.currentIndex + 1
        if (nextIndex < verbList.size) {
            _uiState.value = _uiState.value.copy(
                currentVerb = verbList[nextIndex],
                currentIndex = nextIndex,
                isFlipped = false
            )
        } else {
            loadFirstVerb()
        }
    }

    fun previousVerb() {
        val verbList = if (filteredVerbs.isNotEmpty()) filteredVerbs else verbsRepository.getAllVerbs()
        val prevIndex = _uiState.value.currentIndex - 1
        if (prevIndex >= 0) {
            _uiState.value = _uiState.value.copy(
                currentVerb = verbList[prevIndex],
                currentIndex = prevIndex,
                isFlipped = false
            )
        }
    }

    fun flip() {
        _uiState.value = _uiState.value.copy(isFlipped = !_uiState.value.isFlipped)
    }
}
