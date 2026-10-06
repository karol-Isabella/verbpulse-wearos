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
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Brightness4
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.FreeBreakfast
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.Work
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Text
import com.verbpulse.app.core.designsystem.OrangePrimary
import com.verbpulse.app.core.designsystem.VerbPulseTheme

@Composable
fun HomeScreen() {
    var selectedTheme by remember { mutableStateOf("Tecnología") }
    var selectedWords by remember { mutableStateOf("10") }
    var selectedFrequency by remember { mutableStateOf("1h") }
    var isDarkMode by remember { mutableStateOf(true) }

    HomeContent(
        selectedTheme = selectedTheme,
        onThemeChange = { selectedTheme = it },
        selectedWords = selectedWords,
        onWordsChange = { selectedWords = it },
        selectedFrequency = selectedFrequency,
        onFrequencyChange = { selectedFrequency = it },
        isDarkMode = isDarkMode,
        onToggleDarkMode = { isDarkMode = !isDarkMode }
    )
}

@Composable
fun TopHeader(isDarkMode: Boolean, onToggleDarkMode: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // ES chip
        Box(
            modifier = Modifier
                .background(Color(0xFF2C2C2E), RoundedCornerShape(50.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("ES", color = Color(0xFFEBEBF5), fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.width(8.dp))
        // Sun/Moon chip
        Box(
            modifier = Modifier
                .background(Color(0xFF2C2C2E), RoundedCornerShape(50.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .clickable { onToggleDarkMode() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isDarkMode) Icons.Rounded.WbSunny else Icons.Rounded.Brightness4,
                contentDescription = if (isDarkMode) "Switch to light mode" else "Switch to dark mode",
                tint = Color(0xFFEBEBF5),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun TitleSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.LocalFireDepartment,
                contentDescription = null,
                tint = OrangePrimary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                "Racha de 5 días",
                color = OrangePrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            "Tema de hoy",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ThemeButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) OrangePrimary else Color(0xFF2C2C2E)
    val textColor = if (isSelected) Color.White else Color(0xFF8E8E93)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(bgColor, RoundedCornerShape(50.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else Color(0xFF8E8E93),
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                label,
                color = textColor,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
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
    onSelect: (String) -> Unit
) {
    Column {
        Text(
            label,
            color = Color(0xFF8E8E93),
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(Color(0xFF2C2C2E), RoundedCornerShape(50.dp))
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEach { option ->
                val isActive = option == selected
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (isActive) Color.White else Color.Transparent,
                            RoundedCornerShape(50.dp)
                        )
                        .clickable { onSelect(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        option,
                        color = if (isActive) Color.Black else Color(0xFF8E8E93),
                        fontSize = 13.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
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
    onToggleDarkMode: () -> Unit
) {
    val listState = rememberScalingLazyListState()
    val themes = listOf(
        Triple(Icons.Rounded.Memory, "Tecnología", "Tecnología"),
        Triple(Icons.Rounded.Work, "Negocios", "Negocios"),
        Triple(Icons.Rounded.FreeBreakfast, "Vida Diaria", "Vida Diaria")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp)
        ) {
            item { TopHeader(isDarkMode = isDarkMode, onToggleDarkMode = onToggleDarkMode) }
            item { Spacer(Modifier.height(6.dp)) }
            item { TitleSection() }
            item { Spacer(Modifier.height(8.dp)) }

            items(themes.size) { idx ->
                val (icon, label, key) = themes[idx]
                ThemeButton(
                    icon = icon,
                    label = label,
                    isSelected = selectedTheme == key,
                    onClick = { onThemeChange(key) }
                )
                if (idx < themes.size - 1) Spacer(Modifier.height(8.dp))
            }

            item { Spacer(Modifier.height(10.dp)) }
            item {
                SegmentedSelector(
                    label = "META DIARIA · PALABRAS",
                    options = listOf("5", "10", "15"),
                    selected = selectedWords,
                    onSelect = onWordsChange
                )
            }

            item { Spacer(Modifier.height(10.dp)) }
            item {
                SegmentedSelector(
                    label = "FRECUENCIA · CADA",
                    options = listOf("1h", "2h", "3h"),
                    selected = selectedFrequency,
                    onSelect = onFrequencyChange
                )
            }
        }
    }
}

@Preview(name = "Round Dark", widthDp = 227, heightDp = 227)
@Composable
fun HomeScreenPreviewRound() {
    VerbPulseTheme(darkTheme = true) { HomeScreen() }
}

@Preview(name = "Square Dark", widthDp = 220, heightDp = 220)
@Composable
fun HomeScreenPreviewSquare() {
    VerbPulseTheme(darkTheme = true) { HomeScreen() }
}
