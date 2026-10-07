# Category Persistence & Quiz Integration

This phase consolidates Phase 4 by adding category selection persistence through DataStore and connecting the data flow from HomeScreen category selection through to the learning and quiz screens.

The change adds a `selectedCategory` field to UserPreferences, updates repositories and ViewModels to filter verbs by category with a safe fallback to all verbs, and threads the category through the UI state flow via LaunchedEffect hooks. The build succeeded without errors. The data flow is sound, LaunchedEffect triggers are correct, and Hilt injection is properly separated from preview contexts.

Watch for: The implementation correctly prevents hiltViewModel() in preview functions by extracting composable content into separate functions. Category fallback logic returns all verbs when no verbs match a category, which is safe and intended. All paths leading to quiz and flashcard screens now carry category context from HomePage selection.

**Verdict**: APPROVED

## High-level view

The category selection from HomeScreen (Tecnología, Negocios, Vida Diaria) now persists to DataStore via UserPreferencesRepository and flows back through the app on launch. MainActivity collects the saved category and passes it to both FlashcardScreen and QuizScreen via LaunchedEffect-triggered loadVerbsForCategory() calls in their respective ViewModels. VerbsRepository filters by category and safely falls back to all verbs if a category has no matches, preventing empty verb lists. FlashcardScreen and QuizScreen extract their content into separate composables for preview safety, avoiding hiltViewModel() in @Preview contexts which would crash the annotation processor.

The HomeScreen "Comenzar" and quiz buttons now receive the selected category and invoke callbacks that save the choice before navigating. PreviewLazyColumn state management is safe in both screens: QuizScreen declares rememberScalingLazyListState() and FlashcardScreen uses no list state, both avoiding the crash pattern from earlier phases. All Hilt injection is confined to composable entry points, not preview functions.

<details>
<summary>Issues (0)</summary>

No blocking concerns.

</details>

<details>
<summary>Details</summary>

### Category persistence layer

UserPreferencesRepository adds SELECTED_CATEGORY to DataStore alongside existing theme and language preferences, defaulting to "Tecnología". MainActivity collects userPreferences via collectAsStateWithLifecycle, updating local selectedCategory. When HomeScreen "Comenzar" button fires onStartLearning(category), a coroutine calls updateSelectedCategory before setting showFlashcard=true. The category is persisted and passed as a parameter to downstream screens in the same transaction.

### Verb filtering and category fallback

VerbsRepository tags each of 10 IrregularVerb instances with a category ("Tecnología", "Negocios", or "Vida Diaria") and provides getVerbsByCategory(category: String), which returns matching verbs or falls back to all verbs if the category is empty. This is deliberate and safe: a learner always has verbs to study rather than an empty state.

### FlashcardViewModel lazy-loading

FlashcardViewModel holds filteredVerbs and currentCategory as instance state. loadVerbsByCategory(category) queries VerbsRepository and calls loadFirstVerb() to refresh UI state. LaunchedEffect(selectedCategory) in FlashcardScreen triggers this on every category change, ensuring the correct verb set loads on screen entry or when the user switches categories. Loading deferred from init to an effect prevents loading state before the category context is available.

### Preview safety: hiltViewModel extraction

QuizScreen and FlashcardScreen both extract composable bodies (QuizScreenContent, FlashcardScreenContent) into separate functions with immutable parameters. hiltViewModel() calls remain in the entry composables; preview functions call the content functions directly with hard-coded data, avoiding annotation-processor crashes from running Hilt in preview context.

### Type signature alignment

HomeScreen.onStartLearning is now (String) -> Unit, receiving the selected category. MainActivity's callback passes selectedTheme (the category string) to onStartLearning, which is type-safe. The same applies to onStartQuiz: (String) -> Unit. Both callbacks in MainActivity correctly pass the category before setting navigation flags.

### Category flow through LaunchedEffect

Both FlashcardScreen and QuizScreen use LaunchedEffect(selectedCategory) to trigger loadVerbsForCategory whenever the category parameter changes. The flow is: HomeScreen button → MainActivity callback → updateSelectedCategory to DataStore → collectAsStateWithLifecycle updates selectedCategory → LaunchedEffect fires → ViewModel loads filtered verbs. The trigger key is precise: LaunchedEffect only fires on selectedCategory change, not every recomposition.

### Build success

BUILD SUCCESSFUL in 27 seconds, 43 actionable tasks, 13 executed, 30 up-to-date. No compilation errors, no Hilt failures, no preview crashes.

</details>

## File map

<details>
<summary>Changes</summary>

- **MainActivity.kt**: Added selectedCategory mutable state, collects from UserPreferences, passes category to FlashcardScreen, QuizScreen, and HomeScreen callbacks.
- **UserPreferencesRepository.kt**: Added SELECTED_CATEGORY key, userPreferences flow includes selectedCategory field, added updateSelectedCategory(category) suspend function.
- **UserPreferences.kt** (inferred from Flow mapping): New selectedCategory: String field in data class, default "Tecnología".
- **FlashcardViewModel.kt**: Added filteredVerbs and currentCategory instance fields, added loadVerbsByCategory(category) function, LaunchedEffect in FlashcardScreen calls it.
- **FlashcardScreen.kt**: Extracted FlashcardScreenContent as preview-safe composable, added LaunchedEffect(selectedCategory) to call viewModel.loadVerbsByCategory().
- **QuizViewModel.kt**: Added categoryVerbs instance field, added loadVerbsForCategory(category) function, generateQuestion() samples from categoryVerbs if present.
- **QuizScreen.kt**: Extracted QuizScreenContent as preview-safe composable, added LaunchedEffect(selectedCategory) to call viewModel.loadVerbsForCategory().
- **VerbsRepository.kt**: Added category field to IrregularVerb instances, added getVerbsByCategory(category) function with fallback to all verbs.
- **IrregularVerb.kt**: Added category: String parameter with default "Tecnología".
- **HomeScreen.kt**: Verified callbacks onStartLearning(String) and onStartQuiz(String) receive selectedTheme category parameter.

[Full diff: git diff main](../../../)

</details>
