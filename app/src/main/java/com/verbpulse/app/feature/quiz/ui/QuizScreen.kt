package com.verbpulse.app.feature.quiz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Text
import com.verbpulse.app.core.designsystem.OrangePrimary
import com.verbpulse.app.core.designsystem.VerbPulseTheme
import com.verbpulse.app.feature.learning.data.IrregularVerb

@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    selectedCategory: String = "Tecnología",
    onBack: () -> Unit = {}
) {
    LaunchedEffect(selectedCategory) {
        viewModel.loadVerbsForCategory(selectedCategory)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    QuizScreenContent(
        uiState = uiState,
        onSelectAnswer = { viewModel.selectAnswer(it) },
        onNext = { viewModel.nextQuestion() },
        onBack = onBack
    )
}

@Composable
fun QuizScreenContent(
    uiState: QuizUiState,
    onSelectAnswer: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit = {}
) {
    val listState = rememberScalingLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 10.dp, bottom = 20.dp, start = 8.dp, end = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Question text
            item {
                Text(
                    text = uiState.question,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            item { Spacer(Modifier.height(12.dp)) }

            // Answer options
            items(uiState.options.size) { idx ->
                val option = uiState.options[idx]
                val isSelected = uiState.selectedAnswer == option
                val isCorrect = uiState.isCorrect == true && option == uiState.currentVerb?.past

                val bgColor = when {
                    isSelected && isCorrect -> OrangePrimary
                    isSelected && uiState.isCorrect == false -> Color(0xFFFF0000)
                    isSelected -> Color(0xFFCCCCCC)
                    else -> Color(0xFF1C1C1E)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(44.dp)
                        .background(bgColor, RoundedCornerShape(50.dp))
                        .clickable(enabled = uiState.selectedAnswer == null) { onSelectAnswer(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.Black else Color.White,
                        textAlign = TextAlign.Center
                    )
                }

                if (idx < uiState.options.size - 1) {
                    Spacer(Modifier.height(6.dp))
                }
            }

            item { Spacer(Modifier.height(12.dp)) }

            // Next/Submit button
            item {
                if (uiState.selectedAnswer != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(40.dp)
                            .background(OrangePrimary, RoundedCornerShape(50.dp))
                            .clickable {
                                if (uiState.questionNumber < 10) {
                                    onNext()
                                } else {
                                    onBack()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (uiState.questionNumber < 10) "Siguiente" else "Finalizar",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Round", widthDp = 227, heightDp = 227)
@Composable
fun QuizScreenPreviewRound() {
    VerbPulseTheme(darkTheme = true) {
        QuizScreenContent(
            uiState = QuizUiState(
                question = "What is the past of build?",
                options = listOf("built", "went", "knew"),
                currentVerb = IrregularVerb("build", "built", "built", "construir", "Tecnología")
            ),
            onSelectAnswer = {},
            onNext = {},
            onBack = {}
        )
    }
}

@Preview(name = "Square", widthDp = 220, heightDp = 220)
@Composable
fun QuizScreenPreviewSquare() {
    VerbPulseTheme(darkTheme = true) {
        QuizScreenContent(
            uiState = QuizUiState(
                question = "What is the past of build?",
                options = listOf("built", "went", "knew"),
                currentVerb = IrregularVerb("build", "built", "built", "construir", "Tecnología")
            ),
            onSelectAnswer = {},
            onNext = {},
            onBack = {}
        )
    }
}
