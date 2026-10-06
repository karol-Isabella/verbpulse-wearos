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
    val isFlipped: Boolean = false
)

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val verbsRepository: VerbsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState

    init {
        loadFirstVerb()
    }

    private fun loadFirstVerb() {
        val verb = verbsRepository.getVerbAt(0)
        _uiState.value = _uiState.value.copy(currentVerb = verb, currentIndex = 0)
    }

    fun nextVerb() {
        val nextIndex = _uiState.value.currentIndex + 1
        val verb = verbsRepository.getVerbAt(nextIndex)
        if (verb != null) {
            _uiState.value = _uiState.value.copy(
                currentVerb = verb,
                currentIndex = nextIndex,
                isFlipped = false
            )
        } else {
            // Wrap around to first verb
            loadFirstVerb()
        }
    }

    fun previousVerb() {
        val prevIndex = _uiState.value.currentIndex - 1
        if (prevIndex >= 0) {
            val verb = verbsRepository.getVerbAt(prevIndex)
            if (verb != null) {
                _uiState.value = _uiState.value.copy(
                    currentVerb = verb,
                    currentIndex = prevIndex,
                    isFlipped = false
                )
            }
        }
    }

    fun flip() {
        _uiState.value = _uiState.value.copy(isFlipped = !_uiState.value.isFlipped)
    }
}
