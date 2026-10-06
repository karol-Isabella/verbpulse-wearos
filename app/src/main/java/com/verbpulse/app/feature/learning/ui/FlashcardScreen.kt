package com.verbpulse.app.feature.learning.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Brightness4
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.Text
import com.verbpulse.app.R
import com.verbpulse.app.core.designsystem.OrangeLight
import com.verbpulse.app.core.designsystem.VerbPulseTheme

@Composable
fun FlashcardScreen(
    verb: String = "Build",
    present: String = "build",
    past: String = "built",
    participle: String = "built",
    isDarkMode: Boolean = true,
    language: String = "ES",
    onFlip: () -> Unit = {},
    onBack: () -> Unit = {},
    onThemeChange: () -> Unit = {},
    onLanguageChange: () -> Unit = {}
) {
    val listState = rememberScalingLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
        ) {
            // Header row: time + language/theme chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Time placeholder
                    Text(
                        "14:00",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )

                    // Language and theme chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Language chip
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF2C2C2E), RoundedCornerShape(50.dp))
                                .clickable { onLanguageChange() }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                language,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Theme chip
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF2C2C2E), RoundedCornerShape(50.dp))
                                .clickable { onThemeChange() }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Rounded.WbSunny else Icons.Rounded.Brightness4,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(4.dp)) }

            // Main card: verb with conjugations
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .background(Color(0xFFFF8A65), RoundedCornerShape(28.dp))
                        .padding(vertical = 14.dp, horizontal = 12.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Verb title
                        Text(
                            verb,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(8.dp))

                        // Divider
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(0.5.dp)
                                .background(Color.White.copy(alpha = 0.25f))
                        )

                        Spacer(Modifier.height(8.dp))

                        // Three-column conjugation row
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
                                    stringResource(R.string.flashcard_present_label),
                                    color = Color.White.copy(alpha = 0.65f),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.8.sp
                                )
                                Spacer(Modifier.height(3.dp))
                                Text(
                                    present,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Past column
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    stringResource(R.string.flashcard_past_label),
                                    color = Color.White.copy(alpha = 0.65f),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.8.sp
                                )
                                Spacer(Modifier.height(3.dp))
                                Text(
                                    past,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Participle column
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    stringResource(R.string.flashcard_participle_label),
                                    color = Color.White.copy(alpha = 0.65f),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.8.sp
                                )
                                Spacer(Modifier.height(3.dp))
                                Text(
                                    participle,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }

            // Flip button
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .height(38.dp)
                        .background(Color(0xFF2C2C2E), RoundedCornerShape(50.dp))
                        .clickable { onFlip() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = null,
                            tint = Color(0xFFFF8A65),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            stringResource(R.string.flashcard_flip),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(4.dp)) }

            // Back button
            item {
                Box(
                    modifier = Modifier
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null,
                            tint = Color(0xFF8E8E93),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            stringResource(R.string.flashcard_back),
                            color = Color(0xFF8E8E93),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Round", widthDp = 227, heightDp = 227)
@Composable
fun FlashcardScreenPreviewRound() {
    VerbPulseTheme(darkTheme = true) {
        FlashcardScreen()
    }
}

@Preview(name = "Square", widthDp = 220, heightDp = 220)
@Composable
fun FlashcardScreenPreviewSquare() {
    VerbPulseTheme(darkTheme = true) {
        FlashcardScreen()
    }
}
