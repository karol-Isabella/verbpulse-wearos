package com.verbpulse.app.feature.learning.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
        isDarkMode = isDarkMode,
        onFlip = { viewModel.flip() },
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
    isDarkMode: Boolean = true,
    language: String = "ES",
    onFlip: () -> Unit = {},
    onBack: () -> Unit = {},
    onThemeChange: () -> Unit = {},
    onLanguageChange: () -> Unit = {},
    onStartQuiz: () -> Unit = {}
) {
    // Determine card colour: darker orange in dark mode, lighter in light mode
    val cardColor = if (isDarkMode) Color(0xFFFF5216) else Color(0xFFFF8A65)
    // All text inside the card is black regardless of theme
    val cardTextColor = Color.Black
    val cardLabelColor = Color.Black.copy(alpha = 0.55f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Single card container
        Box(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .background(cardColor, RoundedCornerShape(28.dp))
                .padding(vertical = 18.dp, horizontal = 14.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Verb title — large, bold, black
                Text(
                    text = verb,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = cardTextColor,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(10.dp))

                // Thin divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(Color.Black.copy(alpha = 0.20f))
                )

                Spacer(Modifier.height(10.dp))

                // Three-column conjugation row — all text black
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Present column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.flashcard_present_label),
                            color = cardLabelColor,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = present,
                            color = cardTextColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Past column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.flashcard_past_label),
                            color = cardLabelColor,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = past,
                            color = cardTextColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Participle column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.flashcard_participle_label),
                            color = cardLabelColor,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = participle,
                            color = cardTextColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Flip button — icon ONLY, no text, inside the card, centred
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            Color.Black.copy(alpha = 0.12f),
                            RoundedCornerShape(50.dp)
                        )
                        .clickable { onFlip() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = null,
                        tint = cardTextColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Preview(name = "Round", widthDp = 227, heightDp = 227)
@Composable
fun FlashcardScreenPreviewRound() {
    VerbPulseTheme(darkTheme = true) {
        FlashcardScreenContent()
    }
}

@Preview(name = "Square", widthDp = 220, heightDp = 220)
@Composable
fun FlashcardScreenPreviewSquare() {
    VerbPulseTheme(darkTheme = true) {
        FlashcardScreenContent()
    }
}
