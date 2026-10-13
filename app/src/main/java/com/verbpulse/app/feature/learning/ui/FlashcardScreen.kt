package com.verbpulse.app.feature.learning.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.verbpulse.app.core.designsystem.VerbPulseTheme
import com.verbpulse.app.core.designsystem.WatchControls

@Composable
fun FlashcardScreen(
    viewModel: FlashcardViewModel = hiltViewModel(),
    selectedCategory: String = "Tecnología",
    isDarkMode: Boolean = false,
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
        isPhrasal = uiState.currentVerb?.isPhrasal ?: false,
        isFlipped = uiState.isFlipped,
        currentIndex = uiState.currentIndex,
        totalVerbs = uiState.currentVerbList.size.coerceAtLeast(1),
        category = selectedCategory,
        isDarkMode = isDarkMode,
        language = language,
        onFlip = { viewModel.flip() },
        onNext = { viewModel.nextVerb() },
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
    isPhrasal: Boolean = false,
    isFlipped: Boolean = false,
    currentIndex: Int = 0,
    totalVerbs: Int = 10,
    category: String = "Tecnología",
    isDarkMode: Boolean = false,
    language: String = "ES",
    onFlip: () -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
    onThemeChange: () -> Unit = {},
    onLanguageChange: () -> Unit = {},
    onStartQuiz: () -> Unit = {}
) {
    // 3D rotation animation over the Y axis
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "flashcard_3d_flip"
    )

    // Elegant coral palette
    val cardBrush = if (isPhrasal) {
        Brush.linearGradient(listOf(Color(0xFFFFDCC4), Color(0xFFE8D5FF)))
    } else {
        Brush.linearGradient(listOf(Color(0xFFFFC9AD), Color(0xFFFF8F66)))
    }

    val cardTextColor = if (isPhrasal) Color(0xFF3B1D4D) else Color(0xFF2E0F04)
    val cardLabelColor = cardTextColor.copy(alpha = 0.65f)
    val cardButtonBg = cardTextColor

    val isFront = rotation <= 90f

    val flipLabel = if (language == "ES") "Voltear" else "Flip"
    val gotItLabel = if (language == "ES") "¡Entendido!" else "Got it!"
    val presentLabel = if (language == "ES") "PRESENTE" else "PRESENT"
    val pastLabel = if (language == "ES") "PASADO" else "PAST"
    val participleLabel = if (language == "ES") "PARTICIP." else "PARTICIPLE"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(4.dp))

            // Header con badges ES/EN y Modo Claro/Oscuro
            WatchControls(
                isDarkMode = isDarkMode,
                language = language,
                onToggleTheme = onThemeChange,
                onToggleLanguage = onLanguageChange
            )

            Spacer(Modifier.height(4.dp))

            // Tarjeta de Flashcard más angosta, centrada y con bordes de 24dp
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.84f)
                    .height(148.dp)
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 14f * density
                    }
                    .clip(RoundedCornerShape(24.dp))
                    .background(cardBrush)
                    .clickable { onFlip() }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isFront) {
                    // ====== FRENTE DE LA FLASHCARD ======
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Categoría e índice de palabra
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

                        // Verbo principal grande
                        Text(
                            text = verb,
                            fontSize = if (isPhrasal) 22.sp else 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = cardTextColor,
                            textAlign = TextAlign.Center,
                            lineHeight = if (isPhrasal) 24.sp else 28.sp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )

                        // Si es Phrasal Verb muestra Verbo y Significado; si es Verbo muestra 3 formas
                        if (isPhrasal) {
                            Text(
                                text = translationEs,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = cardTextColor.copy(alpha = 0.85f),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            // Tres formas (Presente, Pasado, Participio)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 2.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = present,
                                        color = cardTextColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = presentLabel,
                                        color = cardLabelColor,
                                        fontSize = 7.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = past,
                                        color = cardTextColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = pastLabel,
                                        color = cardLabelColor,
                                        fontSize = 7.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = participle,
                                        color = cardTextColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = participleLabel,
                                        color = cardLabelColor,
                                        fontSize = 7.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Botón / Hint sutil Voltear
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = flipLabel,
                                tint = cardLabelColor,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(Modifier.width(3.dp))
                            Text(
                                text = flipLabel,
                                color = cardLabelColor,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    // ====== REVERSO DE LA FLASHCARD ======
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                rotationY = 180f
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Traducción destacada
                            Text(
                                text = translationEs.replaceFirstChar { it.uppercase() },
                                color = cardTextColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            )

                            // Badge Gemini AI
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AutoAwesome,
                                    contentDescription = "Gemini AI",
                                    tint = cardTextColor,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(Modifier.width(3.dp))
                                Text(
                                    text = "GEMINI AI",
                                    color = cardLabelColor,
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.6.sp
                                )
                            }

                            // Oración contextualizada
                            Text(
                                text = "“$contextualSentence”",
                                color = cardTextColor,
                                fontSize = 10.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                lineHeight = 13.sp,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )

                            // Botón de acción: ¡Entendido! para avanzar
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(cardButtonBg)
                                    .clickable { onNext() }
                                    .padding(horizontal = 14.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = gotItLabel,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = gotItLabel,
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
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

@Preview(name = "Round Light Front", widthDp = 227, heightDp = 227)
@Composable
fun FlashcardScreenPreviewRoundLightFront() {
    VerbPulseTheme(darkTheme = false) {
        FlashcardScreenContent(isFlipped = false, isDarkMode = false)
    }
}

@Preview(name = "Round Light Flipped", widthDp = 227, heightDp = 227)
@Composable
fun FlashcardScreenPreviewRoundLightFlipped() {
    VerbPulseTheme(darkTheme = false) {
        FlashcardScreenContent(isFlipped = true, isDarkMode = false)
    }
}

@Preview(name = "Round Dark Front Phrasal", widthDp = 227, heightDp = 227)
@Composable
fun FlashcardScreenPreviewRoundDarkFrontPhrasal() {
    VerbPulseTheme(darkTheme = true) {
        FlashcardScreenContent(
            verb = "Figure out",
            isPhrasal = true,
            translationEs = "descifrar, resolver",
            isFlipped = false,
            isDarkMode = true
        )
    }
}
