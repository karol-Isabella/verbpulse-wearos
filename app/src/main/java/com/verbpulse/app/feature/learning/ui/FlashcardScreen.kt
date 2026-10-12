package com.verbpulse.app.feature.learning.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.Text
import com.verbpulse.app.R
import com.verbpulse.app.core.designsystem.VerbPulseTheme

@Composable
fun FlashcardScreen(
    viewModel: FlashcardViewModel = hiltViewModel(),
    selectedCategory: String = "Tecnología",
    isDarkMode: Boolean = true,
    language: String = "ES",
    onFlip: () -> Unit = {},
    onBack: () -> Unit = {},
    onThemeChange: () -> Unit = {},
    onLanguageChange: () -> Unit = {},
    onStartQuiz: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(selectedCategory) {
        viewModel.loadVerbsByCategory(selectedCategory)
    }

    FlashcardScreenContent(
        verb = uiState.currentVerb?.present?.replaceFirstChar { it.uppercase() } ?: "Build",
        present = uiState.currentVerb?.present ?: "build",
        past = uiState.currentVerb?.past ?: "built",
        participle = uiState.currentVerb?.participle ?: "built",
        translationEs = uiState.currentVerb?.translationEs ?: "construir",
        contextualSentence = uiState.contextualSentence,
        isFlipped = uiState.isFlipped,
        currentIndex = uiState.currentIndex,
        totalVerbs = uiState.currentVerbList.size.coerceAtLeast(1),
        category = selectedCategory,
        isDarkMode = isDarkMode,
        language = language,
        onFlip = { viewModel.flip() },
        onNext = { viewModel.nextVerb() },
        onPrevious = { viewModel.previousVerb() },
        onBack = onBack,
        onThemeChange = onThemeChange,
        onLanguageChange = onLanguageChange,
        onStartQuiz = onStartQuiz
    )
}

@Composable
fun FlashcardScreenContent(
    verb: String = "Build",
    present: String = "build",
    past: String = "built",
    participle: String = "built",
    translationEs: String = "construir",
    contextualSentence: String = "We build scalable cloud services.",
    isFlipped: Boolean = false,
    currentIndex: Int = 0,
    totalVerbs: Int = 10,
    category: String = "Tecnología",
    isDarkMode: Boolean = true,
    language: String = "ES",
    onFlip: () -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onBack: () -> Unit = {},
    onThemeChange: () -> Unit = {},
    onLanguageChange: () -> Unit = {},
    onStartQuiz: () -> Unit = {}
) {
    val cardColor = if (isDarkMode) Color(0xFFFF5216) else Color(0xFFFF8A65)
    val cardTextColor = Color.Black
    val cardLabelColor = Color.Black.copy(alpha = 0.58f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .background(cardColor, RoundedCornerShape(28.dp))
                .clickable { onFlip() }
                .padding(vertical = 12.dp, horizontal = 12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header: Categoría e índice de palabra
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.uppercase(),
                        color = cardLabelColor,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.6.sp
                    )
                    Text(
                        text = "${currentIndex + 1}/$totalVerbs",
                        color = cardLabelColor,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(4.dp))

                // Título del verbo
                Text(
                    text = verb,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = cardTextColor,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(6.dp))

                // Divisor sutil
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(Color.Black.copy(alpha = 0.18f))
                )

                Spacer(Modifier.height(6.dp))

                // Contenido alternable según flip (Anverso: Conjugaciones | Reverso: Traducción + Gemini AI)
                AnimatedContent(
                    targetState = isFlipped,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "flashcard_content"
                ) { flipped ->
                    if (!flipped) {
                        // Anverso: Tres columnas de conjugación
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .padding(horizontal = 2.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.flashcard_present_label),
                                    color = cardLabelColor,
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = present,
                                    color = cardTextColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.flashcard_past_label),
                                    color = cardLabelColor,
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = past,
                                    color = cardTextColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.flashcard_participle_label),
                                    color = cardLabelColor,
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = participle,
                                    color = cardTextColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        // Reverso: Traducción + Contextualización Gemini AI (RF-004)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Text(
                                text = translationEs.uppercase(),
                                color = cardTextColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(Modifier.height(2.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AutoAwesome,
                                    contentDescription = "Gemini AI",
                                    tint = cardTextColor,
                                    modifier = Modifier.size(9.dp)
                                )
                                Spacer(Modifier.width(3.dp))
                                Text(
                                    text = "GEMINI AI",
                                    color = cardLabelColor,
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "“$contextualSentence”",
                                color = cardTextColor,
                                fontSize = 10.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                maxLines = 2
                            )
                        }
                    }
                }

                Spacer(Modifier.height(6.dp))

                // Fila de controles rápidos (1 toque)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón anterior
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color.Black.copy(alpha = 0.12f), RoundedCornerShape(50.dp))
                            .clickable { onPrevious() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Anterior",
                            tint = cardTextColor,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Botón Flip central
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(Color.Black.copy(alpha = 0.16f), RoundedCornerShape(50.dp))
                            .clickable { onFlip() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = "Girar",
                            tint = cardTextColor,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    // Botón siguiente
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color.Black.copy(alpha = 0.12f), RoundedCornerShape(50.dp))
                            .clickable { onNext() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = "Siguiente",
                            tint = cardTextColor,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Round Front", widthDp = 227, heightDp = 227)
@Composable
fun FlashcardScreenPreviewRoundFront() {
    VerbPulseTheme(darkTheme = true) {
        FlashcardScreenContent(isFlipped = false)
    }
}

@Preview(name = "Round Flipped", widthDp = 227, heightDp = 227)
@Composable
fun FlashcardScreenPreviewRoundFlipped() {
    VerbPulseTheme(darkTheme = true) {
        FlashcardScreenContent(isFlipped = true)
    }
}

@Preview(name = "Square", widthDp = 220, heightDp = 220)
@Composable
fun FlashcardScreenPreviewSquare() {
    VerbPulseTheme(darkTheme = true) {
        FlashcardScreenContent(isFlipped = false)
    }
}
