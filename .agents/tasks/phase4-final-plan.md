# Phase 4 Final Implementation Plan — Category Persistence & Navigation Flow

**Goal:** Close Phase 4 at 100% by implementing category persistence, filtering verbs by category, and establishing a fluid navigation flow HomeScreen → FlashcardScreen → QuizScreen with real data from VerbsRepository.

---

## Implementation Steps

### 1. Extend UserPreferences.kt with selectedCategory field
Add `selectedCategory: String` field with default "Tecnología".
Files: `app/src/main/java/com/verbpulse/app/core/data/preferences/UserPreferences.kt`
Verify: Compile with `.\gradlew.bat compileDebugKotlin` — no errors.

### 2. Add SELECTED_CATEGORY key and updateSelectedCategory() to UserPreferencesRepository.kt
Add `SELECTED_CATEGORY` stringPreferencesKey, update UserPreferences flow to read it, and add `suspend fun updateSelectedCategory(category: String)` method.
Files: `app/src/main/java/com/verbpulse/app/core/data/preferences/UserPreferencesRepository.kt`
Verify: Compile with `.\gradlew.bat compileDebugKotlin` — no errors.

### 3. Add category field to IrregularVerb.kt
Add `category: String` field to the data class. Assign categories: build/buy/make/take → "Tecnología", do/go/come/give → "Negocios", eat/know → "Vida Diaria".
Files: `app/src/main/java/com/verbpulse/app/feature/learning/data/IrregularVerb.kt`
Verify: Compile with `.\gradlew.bat compileDebugKotlin` — no errors.

### 4. Update VerbsRepository.kt to assign categories and add getVerbsByCategory()
Update the 10 verbs list to include category values. Add `fun getVerbsByCategory(category: String): List<IrregularVerb>` that filters by category and returns all verbs if none match. Update `getRandomWrongAnswers()` to work with the category-filtered list internally.
Files: `app/src/main/java/com/verbpulse/app/feature/learning/data/VerbsRepository.kt`
Verify: Compile with `.\gradlew.bat compileDebugKotlin` — no errors.

### 5. Update FlashcardViewModel.kt to accept category and filter verbs
Add `fun loadVerbsByCategory(category: String)` function that calls `VerbsRepository.getVerbsByCategory()` and stores the filtered list. Modify init to load all verbs by default. Track current verbs list and expose it via uiState (add `currentVerbList: List<IrregularVerb>` to `FlashcardUiState`). Update `nextVerb()`, `previousVerb()` to use the filtered list.
Files: `app/src/main/java/com/verbpulse/app/feature/learning/ui/FlashcardViewModel.kt`
Verify: Compile with `.\gradlew.bat compileDebugKotlin` — no errors.

### 6. Update QuizViewModel.kt to accept category and filter verbs
Add `fun loadVerbsForCategory(category: String)` function that filters verbs from `VerbsRepository.getVerbsByCategory()` before generating questions. Update init to load all verbs by default. Call this function from MainActivity when navigating to QuizScreen.
Files: `app/src/main/java/com/verbpulse/app/feature/quiz/ui/QuizViewModel.kt`
Verify: Compile with `.\gradlew.bat compileDebugKotlin` — no errors.

### 7. Update HomeScreen.kt to pass selectedCategory via callback
Change `onStartLearning` callback signature from `() -> Unit` to `(String) -> Unit`. In the "Comenzar" button onClick, call `onStartLearning(selectedTheme)`. Also update `onStartQuiz` callback to `(String) -> Unit` and call `onStartQuiz(selectedTheme)` in the quiz button.
Files: `app/src/main/java/com/verbpulse/app/feature/setup/ui/HomeScreen.kt`
Verify: Compile with `.\gradlew.bat compileDebugKotlin` — no errors.

### 8. Update MainActivity.kt to collect and pass category
Store `selectedCategory` as a mutableState variable. When `onStartLearning(category)` is called, persist it via `userPreferencesRepository.updateSelectedCategory(category)` and set `showFlashcard = true`. Pass `selectedCategory` to `FlashcardScreen` and `QuizScreen` as a new parameter. Call `FlashcardScreen` with a newly injected ViewModel, passing it the selected category to initialize filtered verbs. Same for `QuizScreen`.
Files: `app/src/main/java/com/verbpulse/app/presentation/MainActivity.kt`
Verify: Compile with `.\gradlew.bat compileDebugKotlin` — no errors.

### 9. Update FlashcardScreen.kt to use FlashcardViewModel and category
Remove hardcoded String params (verb, present, past, participle). Add `viewModel: FlashcardViewModel = hiltViewModel()` and `category: String = "Tecnología"` parameters. Collect `uiState` from ViewModel using `collectAsStateWithLifecycle()`. Display `currentVerb` from ViewModel state. Remove direct parameter displays. On screen enter, call `viewModel.loadVerbsByCategory(category)` to initialize filtered list.
Files: `app/src/main/java/com/verbpulse/app/feature/learning/ui/FlashcardScreen.kt`
Verify: Compile with `.\gradlew.bat compileDebugKotlin` — no errors.

### 10. Fix QuizScreen.kt preview crash by extracting content composable
Extract the QuizScreen body into a new `QuizScreenContent(uiState: QuizUiState, onSelectAnswer: (String) -> Unit, onNext: () -> Unit, onBack: () -> Unit)` composable with no ViewModel dependency. Update `QuizScreen` to become a thin wrapper that injects `hiltViewModel()` and calls `QuizScreenContent`. Update @Preview annotations to call `QuizScreenContent` directly with a fake `QuizUiState`, never calling `hiltViewModel()` in preview. Add `category: String = "Tecnología"` parameter to `QuizScreen` wrapper. On screen enter, call `viewModel.loadVerbsForCategory(category)`.
Files: `app/src/main/java/com/verbpulse/app/feature/quiz/ui/QuizScreen.kt`
Verify: Compile with `.\gradlew.bat compileDebugKotlin` — no errors.

### 11. Build and verify
Run `.\gradlew.bat clean assembleDebug` to ensure the full build succeeds with all category and navigation changes integrated.
Verify: Last line should be "BUILD SUCCESSFUL".

### 12. Commit changes
Commit locally with message: `"feat: phase 4 final - category persistence and navigation flow complete"`.
Do NOT push.
Verify: `git log --oneline -1` shows the new commit.

---

## Design Decisions

### Category Persistence Strategy
**Decision:** Add `selectedCategory` to `UserPreferences` and persist via `UserPreferencesRepository.updateSelectedCategory()`.
**Rationale:** Consistent with existing preference storage pattern (isDarkMode, language, dailyGoal, frequency). Allows category to survive app restarts and enables future reuse of last-selected category.

### Verb-Category Assignment
**Decision:** Assign hardcoded categories in `VerbsRepository` during initialization.
**Rationale:** Avoids database migration complexity at this stage. The 10 verbs are fixed seed data; categories are assigned based on domain logic (Technology: build/buy/make/take; Business: do/go/come/give; Daily Life: eat/know).

### Category Filtering in ViewModels
**Decision:** Add `loadVerbsByCategory(category)` methods in both `FlashcardViewModel` and `QuizViewModel` instead of passing category through SavedStateHandle.
**Rationale:** Simpler call pattern from MainActivity; allows explicit control over when filtering happens. SavedStateHandle would require serialization setup and add complexity; function calls keep the data flow explicit and testable.

### QuizScreen Preview Fix
**Decision:** Extract composable body into `QuizScreenContent` with no ViewModel, wrap `QuizScreen` as a thin shell with `hiltViewModel()`.
**Rationale:** Previews cannot instantiate Hilt components. Separating presentation logic from dependency injection allows @Preview to call `QuizScreenContent` directly with test data. Matches the pattern used in other Wear OS projects.

### Navigation Flow
**Decision:** HomeScreen passes category to MainActivity callback; MainActivity persists it and passes it forward to both screens; screens call `loadVerbsByCategory()` / `loadVerbsForCategory()` on entry.
**Rationale:** Single source of truth (mutableState in MainActivity); each screen independently filters its verb list; no manual prop-drilling beyond category parameter. Ensures that if user navigates back to HomeScreen and selects a different category, both Flashcard and Quiz screens will respect the new selection on next entry.

---

## Ambiguity Resolution

**Q: How to pass category into FlashcardViewModel — SavedStateHandle vs function call?**
A: Function call (`loadVerbsByCategory(category)`) is safer and simpler. SavedStateHandle requires careful serialization and is harder to test. Since MainActivity already knows the selected category and can call the function directly after injecting the ViewModel, there is no need for SavedStateHandle.

**Q: Should FlashcardScreen and QuizScreen call the load function in a LaunchedEffect or immediately on param change?**
A: Immediately on param change (or in composable body before collection). This ensures that if the user navigates back to HomeScreen, selects a new category, and returns, the ViewModel will re-filter. Use `LaunchedEffect(category) { viewModel.loadVerbsByCategory(category) }` to respond to category changes.

**Q: What if a selected category has no verbs?**
A: `getVerbsByCategory()` falls back to returning all verbs if none match the category, ensuring quiz and flashcard always have content to display.

---

## Build & Test Commands

- **Compile:** `.\gradlew.bat compileDebugKotlin`
- **Build:** `.\gradlew.bat clean assembleDebug`
- **Expected output:** "BUILD SUCCESSFUL" on final line.

---

## Commit Template

```
feat: phase 4 final - category persistence and navigation flow complete

- Add selectedCategory field to UserPreferences and repository
- Assign categories to 10 irregular verbs (Technology, Business, Daily Life)
- Add getVerbsByCategory() filtering to VerbsRepository
- Implement category-aware verb loading in FlashcardViewModel and QuizViewModel
- Update HomeScreen to pass selected category via callback
- Update MainActivity to persist category and pass to screens
- Fix FlashcardScreen to use ViewModel instead of hardcoded params
- Extract QuizScreenContent to fix preview crash
- Navigation flow: HomeScreen → FlashcardScreen → QuizScreen with real filtered data
```
