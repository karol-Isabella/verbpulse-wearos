package com.verbpulse.app.core.designsystem

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Shapes

// Minimum touch target: 48dp (Wear OS guideline)
val VerbPulseShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(50.dp)
)

// Specialized shapes for VerbPulse components
val ButtonShape = CircleShape
val ChipShape = RoundedCornerShape(16.dp)
