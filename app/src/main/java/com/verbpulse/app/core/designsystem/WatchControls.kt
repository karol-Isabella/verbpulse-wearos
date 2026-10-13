package com.verbpulse.app.core.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Brightness4
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text

/**
 * Top control badges for Wear OS: Language Toggle (ES/EN) and Theme Toggle (Light/Dark).
 * Follows the sleek Apple Watch badge style with rounded pill containers.
 */
@Composable
fun WatchControls(
    isDarkMode: Boolean,
    language: String,
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val badgeBg = if (isDarkMode) SurfaceDark else SurfaceLight
    val badgeFg = if (isDarkMode) OnSurfaceDark else OnSurfaceLight

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Language Badge (e.g. "ES" / "EN")
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(badgeBg)
                .clickable { onToggleLanguage() }
                .padding(horizontal = 9.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = language.uppercase(),
                color = badgeFg,
                fontSize = 10.sp,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            )
        }

        Row(modifier = Modifier.size(6.dp)) {} // Spacer

        // Theme Toggle Badge (Sun for dark mode, Moon for light mode)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(badgeBg)
                .clickable { onToggleTheme() }
                .padding(all = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isDarkMode) Icons.Rounded.WbSunny else Icons.Rounded.Brightness4,
                contentDescription = if (isDarkMode) "Cambiar a modo claro" else "Cambiar a modo oscuro",
                tint = badgeFg,
                modifier = Modifier.size(13.dp)
            )
        }
    }
}
