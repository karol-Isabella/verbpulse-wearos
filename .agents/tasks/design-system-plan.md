# Design System and Theme Implementation Plan for VerbPulse Wear OS

## Design Decisions

### Color Palette
**Rationale:** The globals.css defines VerbPulse brand colors that blend with the required primary orange (#FF5216). We combine the specified hex colors (#FF5216) with the secondary accents from globals.css (#ff6a3d for dark, #e5461a for light) and standard Material 3 semantic colors for error/success states. This ensures consistency with the web design while meeting Wear OS accessibility on small screens.

**Selected Colors:**
- Primary Orange (Wear main): #FF5216 (as specified)
- Dark mode accent: #ff6a3d (from globals.css .vp-dark)
- Light mode accent: #e5461a (from globals.css .vp-light)
- Light mode: background #ffffff, surface #f0f0f3 (from globals.css .vp-light)
- Dark mode: background #000000, surface #1c1c1e (from globals.css .vp-dark)
- Text on primary: #ffffff (white, per Material 3 standard)
- Error: #B3261E (Material 3 standard red, accessible on Wear OS)

### Typography Scaling
**Rationale:** Wear OS screens are 227-280dp diagonals. We use larger base sizes (display 26sp, title 18sp, body 14sp, label 12sp) optimized for arm-distance viewing. All use Roboto (system default on Wear OS) with proper letter spacing for readability.

### Shape Strategy
**Rationale:** Wear OS supports both round and square screens. We use fully rounded shapes (CircleShape) for buttons to match modern Wear OS design language, 16dp corners for chips (accessible touch), and 8dp for containers (Material 3 default). 48dp minimum touch targets ensure usability with gloved fingers.

### Screen Adaptation
**Rationale:** The isRoundDevice parameter allows runtime detection of screen shape. Theme automatically applies padding and alignment adjustments. Compose's LocalConfiguration provides backup detection if the parameter isn't passed.

---

## Implementation Plan

- [ ] 1. Create `Color.kt` with Material 3 ColorScheme for light and dark themes.
      Define all required colors (primary, onPrimary, secondary, error, surface, background, etc.) using Wear Compose Material3 ColorScheme.
      Files: `app/src/main/java/com/verbpulse/app/core/designsystem/Color.kt`
      Verify: Compile the file with `.\gradlew.bat assembleDebug` and check for no errors. Grep for "ColorScheme" in the output or examine the build log.

- [ ] 2. Create `Typography.kt` with WearTypography configuration for Wear Compose Material3.
      Define TextStyle objects for display, title, body, and label sizes. Use arcLarge, arcMedium, arcSmall for arc-based layouts (Wear OS) plus standard Material 3 sizes.
      Files: `app/src/main/java/com/verbpulse/app/core/designsystem/Typography.kt`
      Verify: Compile with `.\gradlew.bat assembleDebug`. Check that no import errors occur.

- [ ] 3. Create `Shape.kt` with Shapes configuration for Wear Compose Material3.
      Define CornerBasedShape objects: fully rounded (50%) for buttons, 16dp for chips, 8dp for containers and surfaces. Add comment documenting 48dp minimum touch targets.
      Files: `app/src/main/java/com/verbpulse/app/core/designsystem/Shape.kt`
      Verify: Compile with `.\gradlew.bat assembleDebug`. Confirm Shape.kt is recognized.

- [ ] 4. Create `Theme.kt` in `core/designsystem/` with VerbPulseTheme composable.
      Implement VerbPulseTheme accepting isRoundDevice: Boolean and darkTheme: Boolean parameters. Apply ColorScheme, Typography, and Shapes via androidx.wear.compose.material3.MaterialTheme. Include state management for system dark theme detection. Add @Preview functions for both round and square Wear OS screens.
      Files: `app/src/main/java/com/verbpulse/app/core/designsystem/Theme.kt`
      Verify: Compile with `.\gradlew.bat assembleDebug`. Confirm no errors in theme composition or preview definitions.

- [ ] 5. Update `presentation/theme/Theme.kt` to delegate to the new core design system Theme.
      Import VerbPulseTheme from core.designsystem and re-export it, maintaining backward compatibility with existing usage in MainActivity.
      Files: `app/src/main/java/com/verbpulse/app/presentation/theme/Theme.kt`
      Verify: Compile with `.\gradlew.bat assembleDebug`. Run MainActivity preview to ensure VerbPulseApp still renders without errors.

- [ ] 6. Verify the complete build and display debug output.
      Run `.\gradlew.bat assembleDebug` and capture the last 15 lines of output to confirm successful compilation and all design system modules integrated.
      Files: Build output (verification only, no new files)
      Verify: `.\gradlew.bat assembleDebug` completes successfully. Extract and display the last 15 lines of the build log.

---

## File Structure Overview

```
app/src/main/java/com/verbpulse/app/
├── core/
│   └── designsystem/
│       ├── Color.kt              (new)
│       ├── Typography.kt         (new)
│       ├── Shape.kt              (new)
│       └── Theme.kt              (new)
└── presentation/
    └── theme/
        └── Theme.kt              (updated - re-export from designsystem)
```

---

## Design Tokens Reference

### Color Hex Values (for implementation)
- **Primary Orange:** `#FF5216`
- **Dark Accent:** `#ff6a3d`
- **Light Accent:** `#e5461a`
- **Light Background:** `#ffffff`
- **Light Surface:** `#f0f0f3`
- **Dark Background:** `#000000`
- **Dark Surface:** `#1c1c1e`
- **On-Primary (Text):** `#ffffff`
- **Error:** `#B3261E`
- **Success:** `#34d399`
- **Muted Foreground (Light):** `#5c5c63`
- **Muted Foreground (Dark):** `#a1a1a6`

### Typography Sizes
- **Display:** 26 sp (large text for headlines)
- **Title:** 18 sp (section titles)
- **Body:** 14 sp (main content)
- **Label:** 12 sp (buttons, chips, captions)

### Shape & Touch Targets
- **Buttons:** CircleShape (fully rounded, 50% corner radius)
- **Chips:** 16 dp corner radius
- **Containers/Cards:** 8 dp corner radius
- **Minimum Touch Target:** 48 dp (gloved finger accessibility)

### Screen Dimensions (Reference)
- **Round Wear OS:** ~227 dp diameter (1.5 inch Wear smartwatch)
- **Square Wear OS:** ~220 dp (2.5 inch square smartwatch)

---

## Dependencies & Imports Reference

### Required Imports for Each File

**Color.kt:**
```kotlin
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.ColorScheme
```

**Typography.kt:**
```kotlin
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Typography
```

**Shape.kt:**
```kotlin
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
```

**Theme.kt (core/designsystem):**
```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.isSystemInDarkTheme
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.Typography
import androidx.wear.compose.material3.Shapes
```

**Wear Preview Fallback (if WearDevices unavailable):**
```kotlin
import androidx.compose.ui.tooling.preview.Preview
// Fallback: @Preview(widthDp = 227, heightDp = 227) for round
//          @Preview(widthDp = 220, heightDp = 220) for square
```

---

## Notes for Implementation

1. **No breaking changes:** The new designsystem module coexists with presentation/theme. The presentation Theme.kt is updated to re-export, maintaining backward compatibility.

2. **Round vs. Square Detection:** Use LocalConfiguration.current.screenLayout to detect screen shape at runtime if needed, but accept isRoundDevice as a parameter for explicit control.

3. **Wear OS Best Practices:**
   - All interactive elements must be at least 48dp
   - Text sizes should account for arm-distance viewing (larger than phone UIs)
   - Round screens require content padding or alignment to avoid edge cutoff
   - Dark mode is preferred on AMOLED Wear devices for battery life

4. **Material 3 Wear Compose Note:** The Wear Compose Material3 ColorScheme differs from standard Material 3. It includes fields like `surface`, `onSurface`, `surfaceContainer`, etc. Use the exact field names from androidx.wear.compose.material3.ColorScheme.

5. **Preview Devices:** The task specifies fallback to manual dimensions if WearDevices is unavailable. Try importing WearDevices first; if it fails, use the @Preview(widthDp = ..., heightDp = ...) approach.
