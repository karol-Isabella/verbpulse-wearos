package com.verbpulse.app.feature.setup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.FreeBreakfast
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.Work
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.verbpulse.app.core.designsystem.MutedForegroundDark
import com.verbpulse.app.core.designsystem.MutedForegroundLight
import com.verbpulse.app.core.designsystem.OnSurfaceDark
import com.verbpulse.app.core.designsystem.OnSurfaceLight
import com.verbpulse.app.core.designsystem.OrangeCoral
import com.verbpulse.app.core.designsystem.SurfaceDark
import com.verbpulse.app.core.designsystem.SurfaceLight
import com.verbpulse.app.core.designsystem.VerbPulseTheme
import com.verbpulse.app.core.designsystem.WatchControls

@Composable
fun HomeScreen(
    initialCategory: String = "Tecnología",
    initialWords: String = "10",
    initialFrequency: String = "1h",
    onStartLearning: (category: String, words: String, frequency: String) -> Unit = { _, _, _ -> },
    isDarkMode: Boolean = false,
    onThemeChange: () -> Unit = {},
    language: String = "ES",
    onLanguageChange: () -> Unit = {},
    onStartQuiz: (String) -> Unit = {},
    currentStreak: Int = 1,
    onOpenProgress: () -> Unit = {}
) {
    var selectedTheme by remember(initialCategory) { mutableStateOf(initialCategory) }
    var selectedWords by remember(initialWords) { mutableStateOf(initialWords) }
    var selectedFrequency by remember(initialFrequency) { mutableStateOf(initialFrequency) }

    HomeContent(
        selectedTheme = selectedTheme,
        onThemeChange = { selectedTheme = it },
        selectedWords = selectedWords,
        onWordsChange = { selectedWords = it },
        selectedFrequency = selectedFrequency,
        onFrequencyChange = { selectedFrequency = it },
        isDarkMode = isDarkMode,
        onToggleDarkMode = onThemeChange,
        language = language,
        onLanguageChange = onLanguageChange,
        onStartLearning = { onStartLearning(selectedTheme, selectedWords, selectedFrequency) },
        onStartQuiz = onStartQuiz,
        currentStreak = currentStreak,
        onOpenProgress = onOpenProgress
    )
}

@Composable
fun TitleSection(
    streakDays: Int = 1,
    language: String = "ES",
    onOpenProgress: () -> Unit = {}
) {
    val streakText = if (language == "ES") {
        if (streakDays > 0) "Racha de $streakDays días" else "Comienza tu racha"
    } else {
        if (streakDays > 0) "$streakDays Day Streak" else "Start your streak"
    }

    val titleText = if (language == "ES") "Tema de hoy" else "Today's Topic"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onOpenProgress() }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.LocalFireDepartment,
                contentDescription = null,
                tint = OrangeCoral,
                modifier = Modifier.size(13.dp)
            )
            Spacer(Modifier.width(3.dp))
            Text(
                text = streakText,
                color = OrangeCoral,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.height(2.dp))
        Text(
            text = titleText,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ThemeButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) OrangeCoral else (if (isDarkMode) SurfaceDark else SurfaceLight)
    val textColor = if (isSelected) Color.White else (if (isDarkMode) OnSurfaceDark else OnSurfaceLight)
    val iconColor = if (isSelected) Color.White else (if (isDarkMode) OnSurfaceDark else OnSurfaceLight)

    Box(
        modifier = Modifier
            .fillMaxWidth(0.82f)
            .height(30.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(bgColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(7.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

@Composable
fun SegmentedSelector(
    label: String,
    options: List<String>,
    selected: String,
    isDarkMode: Boolean,
    onSelect: (String) -> Unit
) {
    val containerBg = if (isDarkMode) SurfaceDark else SurfaceLight
    val activeBg = if (isDarkMode) Color.White else Color.Black
    val activeText = if (isDarkMode) Color.Black else Color.White
    val inactiveText = if (isDarkMode) MutedForegroundDark else MutedForegroundLight
    val labelColor = if (isDarkMode) MutedForegroundDark else MutedForegroundLight

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            color = labelColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(3.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(containerBg)
                .padding(2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEach { option ->
                val isActive = option == selected
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(38.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(if (isActive) activeBg else Color.Transparent)
                        .clickable { onSelect(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        color = if (isActive) activeText else inactiveText,
                        fontSize = 11.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun HomeContent(
    selectedTheme: String,
    onThemeChange: (String) -> Unit,
    selectedWords: String,
    onWordsChange: (String) -> Unit,
    selectedFrequency: String,
    onFrequencyChange: (String) -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    language: String,
    onLanguageChange: () -> Unit,
    onStartLearning: (String) -> Unit,
    onStartQuiz: (String) -> Unit = {},
    currentStreak: Int = 1,
    onOpenProgress: () -> Unit = {}
) {
    val listState = rememberScalingLazyListState()

    val themes = if (language == "ES") {
        listOf(
            Triple(Icons.Rounded.Memory, "Tecnología", "Tecnología"),
            Triple(Icons.Rounded.Work, "Negocios", "Negocios"),
            Triple(Icons.Rounded.FreeBreakfast, "Vida Diaria", "Vida Diaria")
        )
    } else {
        listOf(
            Triple(Icons.Rounded.Memory, "Technology", "Tecnología"),
            Triple(Icons.Rounded.Work, "Business", "Negocios"),
            Triple(Icons.Rounded.FreeBreakfast, "Daily Life", "Vida Diaria")
        )
    }

    val goalLabel = if (language == "ES") "META DIARIA · PALABRAS" else "DAILY GOAL · WORDS"
    val freqLabel = if (language == "ES") "FRECUENCIA · CADA" else "FREQUENCY · EVERY"
    val startBtnText = if (language == "ES") "Comenzar" else "Start"
    val quizBtnText = "Quiz"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 4.dp, bottom = 26.dp, start = 8.dp, end = 8.dp)
        ) {
            item {
                WatchControls(
                    isDarkMode = isDarkMode,
                    language = language,
                    onToggleTheme = onToggleDarkMode,
                    onToggleLanguage = onLanguageChange
                )
            }
            item { Spacer(Modifier.height(3.dp)) }
            item {
                TitleSection(
                    streakDays = currentStreak,
                    language = language,
                    onOpenProgress = onOpenProgress
                )
            }
            item { Spacer(Modifier.height(5.dp)) }

            items(themes.size) { idx ->
                val (icon, label, key) = themes[idx]
                ThemeButton(
                    icon = icon,
                    label = label,
                    isSelected = selectedTheme == key,
                    isDarkMode = isDarkMode,
                    onClick = { onThemeChange(key) }
                )
                if (idx < themes.size - 1) Spacer(Modifier.height(3.dp))
            }

            item { Spacer(Modifier.height(6.dp)) }
            item {
                SegmentedSelector(
                    label = goalLabel,
                    options = listOf("5", "10", "15"),
                    selected = selectedWords,
                    isDarkMode = isDarkMode,
                    onSelect = onWordsChange
                )
            }

            item { Spacer(Modifier.height(6.dp)) }
            item {
                SegmentedSelector(
                    label = freqLabel,
                    options = listOf("1h", "2h", "3h"),
                    selected = selectedFrequency,
                    isDarkMode = isDarkMode,
                    onSelect = onFrequencyChange
                )
            }

            item { Spacer(Modifier.height(8.dp)) }
            item {
                // Primary Action Button: Comenzar
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.82f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(OrangeCoral)
                        .clickable(onClick = { onStartLearning(selectedTheme) }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = startBtnText,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item { Spacer(Modifier.height(4.dp)) }
            item {
                // Secondary Action Button: Quiz
                val quizBg = if (isDarkMode) SurfaceDark else SurfaceLight
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.82f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(quizBg)
                        .clickable(onClick = { onStartQuiz(selectedTheme) }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = quizBtnText,
                        color = OrangeCoral,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(name = "Round Light", widthDp = 227, heightDp = 227)
@Composable
fun HomeScreenPreviewRoundLight() {
    VerbPulseTheme(darkTheme = false) { HomeScreen(isDarkMode = false, language = "ES") }
}

@Preview(name = "Round Dark", widthDp = 227, heightDp = 227)
@Composable
fun HomeScreenPreviewRoundDark() {
    VerbPulseTheme(darkTheme = true) { HomeScreen(isDarkMode = true, language = "ES") }
}