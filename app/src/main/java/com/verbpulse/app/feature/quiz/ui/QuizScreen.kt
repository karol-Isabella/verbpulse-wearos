package com.verbpulse.app.feature.quiz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.verbpulse.app.core.designsystem.OnSurfaceDark
import com.verbpulse.app.core.designsystem.OnSurfaceLight
import com.verbpulse.app.core.designsystem.OrangeFlame
import com.verbpulse.app.core.designsystem.RedVibrantDark
import com.verbpulse.app.core.designsystem.RedVibrantLight
import com.verbpulse.app.core.designsystem.SuccessGreen
import com.verbpulse.app.core.designsystem.SurfaceDark
import com.verbpulse.app.core.designsystem.SurfaceLight
import com.verbpulse.app.core.designsystem.VerbPulseTheme
import com.verbpulse.app.core.designsystem.WatchControls
import com.verbpulse.app.feature.learning.data.IrregularVerb

@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    selectedCategory: String = "Tecnología",
    isDarkMode: Boolean = false,
    language: String = "ES",
    onThemeChange: () -> Unit = {},
    onLanguageChange: () -> Unit = {},
    onBack: () -> Unit = {},
    onFinishQuiz: () -> Unit = onBack
) {
    LaunchedEffect(selectedCategory) {
        viewModel.loadVerbsForCategory(selectedCategory)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) {
            onFinishQuiz()
        }
    }

    QuizScreenContent(
        uiState = uiState,
        isDarkMode = isDarkMode,
        language = language,
        onToggleTheme = onThemeChange,
        onToggleLanguage = onLanguageChange,
        onSelectAnswer = { viewModel.selectAnswer(it) },
        onBack = onBack
    )
}

@Composable
fun QuizScreenContent(
    uiState: QuizUiState,
    isDarkMode: Boolean = false,
    language: String = "ES",
    onToggleTheme: () -> Unit = {},
    onToggleLanguage: () -> Unit = {},
    onSelectAnswer: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val listState = rememberScalingLazyListState()

    val titleLabel = if (language == "ES") {
        "REPASO DIARIO ${uiState.questionNumber}/${uiState.totalQuestions}"
    } else {
        "DAILY REVIEW ${uiState.questionNumber}/${uiState.totalQuestions}"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 4.dp, bottom = 20.dp, start = 4.dp, end = 4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Header con badges ES/EN y Modo Claro/Oscuro
            item {
                WatchControls(
                    isDarkMode = isDarkMode,
                    language = language,
                    onToggleTheme = onToggleTheme,
                    onToggleLanguage = onToggleLanguage
                )
            }

            item { Spacer(Modifier.height(3.dp)) }

            // Subtítulo naranja Flame: REPASO DIARIO 1/5
            item {
                Text(
                    text = titleLabel,
                    color = OrangeFlame,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.6.sp
                )
            }

            item { Spacer(Modifier.height(2.dp)) }

            // Pregunta destacada en negrita
            item {
                Text(
                    text = uiState.question,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }

            item { Spacer(Modifier.height(8.dp)) }

            // Opciones de respuesta elegantes con espaciado vertical de 8dp y padding horizontal de 16dp
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    uiState.options.forEach { option ->
                        val isSelected = uiState.selectedAnswer == option
                        val isCorrect = option.equals(uiState.correctAnswer, ignoreCase = true)
                        val revealed = uiState.selectedAnswer != null

                        // Rojo Vivo e Intenso (#FF3B30 en oscuro, #D32F2F en fondo claro)
                        val vibrantErrorRed = if (isDarkMode) RedVibrantDark else RedVibrantLight

                        val (bgColor, textColor, icon, alpha) = when {
                            revealed && isCorrect -> {
                                Quad(SuccessGreen, Color.White, Icons.Rounded.Check, 1f)
                            }
                            revealed && isSelected && !isCorrect -> {
                                Quad(vibrantErrorRed, Color.White, Icons.Rounded.Close, 1f)
                            }
                            revealed && !isSelected && isCorrect -> {
                                Quad(SuccessGreen, Color.White, Icons.Rounded.Check, 1f)
                            }
                            revealed && !isSelected && !isCorrect -> {
                                Quad(
                                    if (isDarkMode) SurfaceDark else SurfaceLight,
                                    if (isDarkMode) OnSurfaceDark else OnSurfaceLight,
                                    null,
                                    0.45f
                                )
                            }
                            else -> {
                                Quad(
                                    if (isDarkMode) SurfaceDark else SurfaceLight,
                                    if (isDarkMode) OnSurfaceDark else OnSurfaceLight,
                                    null,
                                    1f
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .alpha(alpha)
                                .clip(RoundedCornerShape(22.dp))
                                .background(bgColor)
                                .clickable(enabled = !revealed) { onSelectAnswer(option) },
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = option,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textColor,
                                    modifier = Modifier.weight(1f)
                                )
                                if (icon != null) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = if (isCorrect) "Correcto" else "Incorrecto",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Preview(name = "Quiz Light Mode", widthDp = 227, heightDp = 227)
@Composable
fun QuizScreenPreviewLight() {
    VerbPulseTheme(darkTheme = false) {
        QuizScreenContent(
            uiState = QuizUiState(
                question = "\"Figure out\" means…",
                options = listOf("Rendirse", "Descifrar", "Olvidar"),
                correctAnswer = "Descifrar",
                currentVerb = IrregularVerb("figure out", "figured out", "figured out", "descifrar", "Tecnología", isPhrasal = true)
            ),
            isDarkMode = false
        )
    }
}

@Preview(name = "Quiz Dark Mode Validated", widthDp = 227, heightDp = 227)
@Composable
fun QuizScreenPreviewDarkValidated() {
    VerbPulseTheme(darkTheme = true) {
        QuizScreenContent(
            uiState = QuizUiState(
                question = "\"Figure out\" means…",
                options = listOf("Rendirse", "Descifrar", "Olvidar"),
                correctAnswer = "Descifrar",
                selectedAnswer = "Rendirse",
                isCorrect = false,
                currentVerb = IrregularVerb("figure out", "figured out", "figured out", "descifrar", "Tecnología", isPhrasal = true)
            ),
            isDarkMode = true
        )
    }
}
