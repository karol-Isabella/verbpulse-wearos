# HomeScreen Refactor Implementation Plan

## Objective
Refactor `HomeScreen.kt` to be pixel-perfect Apple minimalist design matching the V0 prototype, with proper segmented selectors, vertical theme list, and precise spacing/typography.

## Design System Context (Already Implemented)
- **Colors**: OrangePrimary (#FF5216), Black (#000000), White (#FFFFFF), OutlineDark (#8E8E93), SurfaceDark (#1C1C1E), Color(0xFF2C2C2E) dark gray
- **Typography**: VerbPulseTypography defined in Typography.kt with labelSmall = 10sp, standard sizes
- **Shapes**: VerbPulseShapes with extraLarge = RoundedCornerShape(50.dp)
- **Theme**: VerbPulseTheme wrapping Wear Compose Material3 MaterialTheme
- **Libraries**: wear.compose.foundation (ScalingLazyColumn), wear.compose.material3 (Material3 components)

## Files to Modify/Create

### 1. **app/src/main/java/com/verbpulse/app/feature/setup/ui/HomeScreen.kt** (Complete Rewrite)
   - Replace entire file with new pixel-perfect implementation
   - Decompose into smaller composables for maintainability
   - Add proper state management and composition hierarchy

### 2. **app/src/main/res/values/strings.xml** (Update)
   - Verify/add all string resources for the new design

## Implementation Steps

### Step 1: Update strings.xml with Required String Resources
   **What**: Add missing string resources for the new HomeScreen design that will be referenced in the composable.
   
   **Files**: `app/src/main/res/values/strings.xml`
   
   **Verify**: Run `grep -r "home_theme_tecnologia" app/src/main/res/values/strings.xml` and confirm all theme strings exist.

---

### Step 2: Rewrite HomeScreen.kt with Pixel-Perfect Design
   **What**: Replace the entire HomeScreen.kt with a new implementation following the V0 prototype spec. 
   
   Implement the following structure:
   
   - **Main Composable: HomeScreen()**
     - Hold all state: selectedTheme, selectedWords, selectedFrequency, isDarkMode
     - Call HomeContent() with state parameters
     - Include @Preview annotations for round and square devices (227x227 and 220x220)
   
   - **HomeContent(selectedTheme, selectedWords, selectedFrequency, isDarkMode)**
     - Use ScalingLazyColumn with:
       - Background: MaterialTheme.colorScheme.background (Black in dark mode)
       - contentPadding: PaddingValues(horizontal = 12.dp, top = 8.dp, bottom = 16.dp)
       - horizontalAlignment = Alignment.CenterHorizontally
       - scalingParams: default (to avoid aggressive scaling on small screens)
       - verticalArrangement: Arrangement.spacedBy(0.dp) — spacing managed per item
   
   - **TopHeader(isDarkMode)**
     - Centered Row with two chips side by side, ~8dp gap
     - Left chip: "ES" text, background Color(0xFF2C2C2E), capsule RoundedCornerShape(50.dp)
     - Right chip: "☀️" emoji, same background/shape
     - Both chips: height ~28dp, horizontal padding ~12dp, text ~11sp, white/light gray color
   
   - **TitleSection()**
     - Centered Column, ~16dp top/bottom spacing
     - Line 1: "🔥 Racha de 5 días" in OrangePrimary, ~11sp, FontWeight.Normal
     - Line 2: "Tema de hoy" in White, ~16sp, FontWeight.Bold
     - ~4dp gap between lines
   
   - **ThemeSelection(selectedTheme, onThemeChange)**
     - Vertical list (ScalingLazyColumn item for each theme)
     - 3 themes: "Tecnología" (⚙️), "Negocios" (💼), "Vida Diaria" (☕)
     - Each button: fillMaxWidth, height ~48dp, RoundedCornerShape(50.dp)
     - **Active theme**:
       - Background: OrangePrimary
       - Row: [Icon 20dp] [Spacer 12dp] [Text left-aligned, White, Bold, 13sp] [Spacer weight 1f] [✓ White 13sp] [12dp end padding]
     - **Inactive theme**:
       - Background: Color(0xFF2C2C2E)
       - Row: [Icon 20dp] [Spacer 12dp] [Text left-aligned, OutlineDark, Normal, 13sp] [Spacer weight 1f] [no checkmark]
     - 8dp vertical spacing between buttons
   
   - **SegmentedSelector(label, options, selectedIndex, onSelection)**
     - Reusable composable for both META DIARIA and FRECUENCIA selectors
     - Label: uppercase 10sp, FontWeight.Normal, Color(0xFF8E8E93)
     - ~8dp gap between label and control
     - Container: fillMaxWidth, background Color(0xFF2C2C2E), RoundedCornerShape(50.dp), height ~36dp
     - Each segment: equal weight, height 100%
     - **Selected segment**: White background (#FFFFFF), black text, FontWeight.Bold, ~13sp, round pill shape with ~4dp inner padding
     - **Unselected segment**: transparent background, Color(0xFF8E8E93) text, ~13sp
     - ~20dp top spacing before label
   
   - **SegmentedSelector for META DIARIA**:
     - Label: "META DIARIA · PALABRAS"
     - Options: ["5", "10", "15"]
     - Default selected: "10" (index 1)
   
   - **SegmentedSelector for FRECUENCIA**:
     - Label: "FRECUENCIA · CADA"
     - Options: ["1h", "2h", "3h"]
     - Default selected: "1h" (index 0)
   
   **Import Statements Required**:
   ```kotlin
   import androidx.compose.foundation.background
   import androidx.compose.foundation.clickable
   import androidx.compose.foundation.layout.*
   import androidx.compose.foundation.shape.RoundedCornerShape
   import androidx.compose.runtime.*
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
   import androidx.wear.compose.material3.MaterialTheme
   import androidx.wear.compose.material3.Text
   import com.verbpulse.app.R
   import com.verbpulse.app.core.designsystem.OrangePrimary
   import com.verbpulse.app.core.designsystem.OutlineDark
   import com.verbpulse.app.core.designsystem.VerbPulseTheme
   import com.verbpulse.app.core.designsystem.White
   import com.verbpulse.app.core.designsystem.Black
   ```
   
   **Files**: `app/src/main/java/com/verbpulse/app/feature/setup/ui/HomeScreen.kt`
   
   **Verify**: 
   - Run `.\gradlew.bat assembleDebug` from the workspace root
   - Confirm build succeeds with no errors
   - Show the last 15 lines of the build output
   - Check that the preview annotations are valid (no IDE warnings about @Preview)

---

## Key Design Decisions

1. **Segmented Selector Implementation**: Instead of separate Row buttons, use a single container (Color(0xFF2C2C2E)) with individually tappable segments inside. The selected segment gets a white pill-shaped background. This matches Apple minimalist design and the V0 prototype spec.

2. **Vertical Theme List**: Themes are now in a vertical list (full-width buttons, one per row) instead of horizontal chips, providing better touch targets for Wear OS and clearer visual hierarchy.

3. **Composable Decomposition**: Split HomeScreen into smaller, reusable pieces (TopHeader, TitleSection, ThemeSelection, SegmentedSelector) to keep code readable and maintainable. This is an Apple design best practice for modularity.

4. **Color Palette**: Using the existing design system colors (OrangePrimary, Black, White, OutlineDark, Color(0xFF2C2C2E) for subtle backgrounds). No new color definitions needed.

5. **Typography**: Leverage existing VerbPulseTypography sizes. Custom sizes (10sp, 11sp, 13sp, 16sp) are applied inline to match the exact pixel-perfect spec.

6. **ScalingLazyColumn**: Use default scaling params to avoid aggressive font/component scaling on small Wear screens, ensuring consistent pixel-perfect appearance.

7. **State Management**: Local `mutableStateOf` for selected theme, words, and frequency. No external ViewModel needed for this simple screen; can be added later if needed for persistence.

---

## Build & Verification

**Build Command**: `.\gradlew.bat assembleDebug`

**Expected Outcome**:
- Build completes successfully (Exit code 0)
- No compilation errors
- HomeScreen.kt compiles with all Compose and Wear dependencies available
- Preview annotations render without warnings

**Testing Strategy**:
- Previews will show the design on round (227x227) and square (220x220) Wear devices
- Verify visual alignment: header chips centered, title text sizes, theme buttons full-width with icons/checkmarks, segmented selectors with pill-shaped selection
- Verify spacing: 8dp gaps between items, 20dp before segment labels, 4dp between title lines

---

## Rollback Plan

If compilation fails, revert HomeScreen.kt to the previous version. The existing implementation is known to compile, and all design system dependencies are stable.

---

## Notes

- This is a complete file replacement; the old HomeScreen.kt will be completely overwritten.
- No new dependencies are needed; all Wear Compose and Material3 libraries are already in `app/build.gradle.kts`.
- The design is optimized for Wear OS screens (round and square, both ~220-227dp).
- The Apple minimalist aesthetic is achieved through: clean spacing, subtle color use (gray backgrounds for inactive states), bold typography for emphasis (white on orange for active), and rounded pill shapes (capsules).
