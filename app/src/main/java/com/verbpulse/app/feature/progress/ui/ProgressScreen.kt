package com.verbpulse.app.feature.progress.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.School
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.verbpulse.app.core.designsystem.OrangePrimary
import com.verbpulse.app.core.designsystem.VerbPulseTheme

@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel = hiltViewModel(),
    onDone: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProgressScreenContent(
        scorePercentage = uiState.latestScorePercentage,
        streakDays = uiState.streakDays,
        totalWords = uiState.totalWordsLearned,
        averagePercentage = uiState.averageScorePercentage,
        category = uiState.latestCategory,
        onDone = onDone
    )
}

@Composable
fun ProgressScreenContent(
    scorePercentage: Float = 80f,
    streakDays: Int = 5,
    totalWords: Int = 10,
    averagePercentage: Float = 85f,
    category: String = "Tecnología",
    onDone: () -> Unit = {}
) {
    val listState = rememberScalingLazyListState()
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(scorePercentage) {
        val target = (scorePercentage / 100f).coerceIn(0f, 1f)
        animatedProgress.animateTo(
            targetValue = target,
            animationSpec = tween(durationMillis = 900)
        )
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
            contentPadding = PaddingValues(top = 10.dp, bottom = 24.dp, start = 10.dp, end = 10.dp)
        ) {
            // Título de la pantalla
            item {
                Text(
                    text = "RESUMEN DEL DÍA",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }

            item { Spacer(Modifier.height(4.dp)) }

            // Anillo gráfico circular de aciertos (RF-006)
            item {
                val trackColor = MaterialTheme.colorScheme.secondaryContainer
                Box(
                    modifier = Modifier.size(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(80.dp)) {
                        val strokeWidth = 8.dp.toPx()
                        // Pista de fondo
                        drawCircle(
                            color = trackColor,
                            radius = (size.minDimension - strokeWidth) / 2,
                            style = Stroke(width = strokeWidth)
                        )
                        // Arco de progreso activo
                        drawArc(
                            color = OrangePrimary,
                            startAngle = -90f,
                            sweepAngle = 360f * animatedProgress.value,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${scorePercentage.toInt()}%",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Aciertos",
                            fontSize = 8.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }

            // Tarjeta de Racha Diaria (RF-007)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(20.dp))
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.LocalFireDepartment,
                                contentDescription = "Racha",
                                tint = OrangePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Racha activa",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = "$streakDays días",
                            fontSize = 12.sp,
                            color = OrangePrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(4.dp)) }

            // Estadísticas históricas acumuladas (RF-008)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(0.92f),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Palabras repasadas
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(18.dp))
                            .padding(vertical = 8.dp, horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Rounded.School,
                                contentDescription = null,
                                tint = Color(0xFF64D2FF),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "$totalWords",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Palabras",
                                fontSize = 7.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Promedio histórico
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(18.dp))
                            .padding(vertical = 8.dp, horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF30D158),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "${averagePercentage.toInt()}%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Promedio",
                                fontSize = 7.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }

            // Botón de finalización (1 toque - RNF-005)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(38.dp)
                        .background(OrangePrimary, RoundedCornerShape(50.dp))
                        .clickable(onClick = onDone),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Volver al Inicio",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(name = "Round", widthDp = 227, heightDp = 227)
@Composable
fun ProgressScreenPreviewRound() {
    VerbPulseTheme(darkTheme = true) {
        ProgressScreenContent()
    }
}

@Preview(name = "Square", widthDp = 220, heightDp = 220)
@Composable
fun ProgressScreenPreviewSquare() {
    VerbPulseTheme(darkTheme = true) {
        ProgressScreenContent()
    }
}
