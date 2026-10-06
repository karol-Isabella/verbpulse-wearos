# Implementation Plan: Stage 4 — Data Layer, ViewModels, and Quiz Integration

## Design Decisions

1. **DataStore vs Room for Preferences**: Using DataStore (Preferences) rather than Room for user settings (isDarkMode, language, dailyGoal, frequency) because preferences are simple key-value pairs with no complex queries. This reduces boilerplate and runtime overhead on a Wear OS device.

2. **In-Memory Verbs List**: VerbsRepository will hold a hardcoded list of 10 irregular verbs in memory with no persistence layer. No Room database needed for this stage. If persistence becomes needed later, it's a straightforward migration to Room.

3. **ViewModel Layer**: FlashcardViewModel and QuizViewModel will be simple Hilt-injected ViewModels managing UI state (currentIndex, selectedAnswer, etc.) and side effects (loading verbs). They will expose StateFlow<> properties for Compose to consume via collectAsStateWithLifecycle().

4. **Navigation State**: The navigation flow (Home → Flashcard → Quiz) will remain in MainActivity.kt as simple Boolean state flags (showFlashcard, showQuiz) rather than introducing NavController, keeping the implementation minimal and Wear OS-focused.

5. **String Resources**: All user-facing text (quiz labels, verb tenses, button labels) moved to strings.xml for localization readiness and to avoid hardcoding.

## Implementation Plan

---

- [ ] **1. Create UserPreferencesRepository with DataStore integration.**
      
      Implement a Hilt @Singleton service in `core/data` that reads/writes user preferences (isDarkMode, language, dailyGoal, frequency) using androidx.datastore.preferences. Expose StateFlow properties for each preference so Compose can observe changes reactively.
      
      **Files:**
      - `c:\5_semestre\trabajos-finales\VerbPulse\app\src\main\java\com\verbpulse\app\core\data\preferences\UserPreferencesRepository.kt` (create)
      
      **Verify:**
      ```
      .\gradlew.bat assembleDebug
      ```
      Build must complete without errors. No new unit tests required yet (state management tested end-to-end in MainActivity).

---

- [ ] **2. Create VerbsRepository with hardcoded irregular verbs list.**
      
      Implement a Hilt @Singleton repository in `feature/learning/data` that returns a fixed list of 10 irregular English verbs, each with: present tense, past tense, participle, and Spanish translation. Data structure: `data class Verb(val present: String, val past: String, val participle: String, val translationEs: String)`.
      
      **Files:**
      - `c:\5_semestre\trabajos-finales\VerbPulse\app\src\main\java\com\verbpulse\app\feature\learning\data\VerbsRepository.kt` (create)
      - `c:\5_semestre\trabajos-finales\VerbPulse\app\src\main\java\com\verbpulse\app\feature\learning\data\Verb.kt` (create)
      
      **Verify:**
      ```
      .\gradlew.bat assembleDebug
      ```
      Build must complete. Repo is simple and testable, but no unit tests required for this stage.

---

- [ ] **3. Create FlashcardViewModel for flashcard state management.**
      
      Implement a Hilt-injected ViewModel in `feature/learning/ui` that:
      - Injects VerbsRepository
      - Exposes StateFlow<Verb> for current verb
      - Exposes StateFlow<Int> for currentIndex
      - Provides nextCard() and previousCard() functions
      - Provides nextQuestion() to navigate to quiz (called by button on FlashcardScreen)
      
      **Files:**
      - `c:\5_semestre\trabajos-finales\VerbPulse\app\src\main\java\com\verbpulse\app\feature\learning\ui\FlashcardViewModel.kt` (create)
      
      **Verify:**
      ```
      .\gradlew.bat assembleDebug
      ```
      Build must complete.

---

- [ ] **4. Create QuizViewModel for quiz state management.**
      
      Implement a Hilt-injected ViewModel in `feature/quiz/ui` that:
      - Injects VerbsRepository
      - Exposes StateFlow<Verb> for current question verb
      - Exposes StateFlow<List<String>> for answer options (3 randomly selected)
      - Exposes StateFlow<String?> for selectedAnswer
      - Provides selectAnswer(answer: String) to record user choice
      - Provides nextQuestion() for advancing to next question
      
      **Files:**
      - `c:\5_semestre\trabajos-finales\VerbPulse\app\src\main\java\com\verbpulse\app\feature\quiz\ui\QuizViewModel.kt` (create)
      
      **Verify:**
      ```
      .\gradlew.bat assembleDebug
      ```
      Build must complete.

---

- [ ] **5. Create QuizScreen Composable for quiz UI (Wear OS layout).**
      
      Implement a Wear OS Compose screen in `feature/quiz/ui` displaying:
      - Question: "What is the past tense of [verb]?"
      - 3 answer options in a ScalingLazyColumn (one per row, full width)
      - Selected answer highlighted (orange border or background)
      - Submit/Next button at bottom
      - Include @Preview annotations for round and square devices
      - Use RoundedCornerShape(28.dp) for option boxes matching FlashcardScreen style
      - Consume QuizViewModel state via collectAsStateWithLifecycle()
      
      **Files:**
      - `c:\5_semestre\trabajos-finales\VerbPulse\app\src\main\java\com\verbpulse\app\feature\quiz\ui\QuizScreen.kt` (create)
      
      **Verify:**
      ```
      .\gradlew.bat assembleDebug
      ```
      Build must complete. Preview renders both round and square devices.

---

- [ ] **6. Add strings.xml entries for quiz and verb terminology.**
      
      Add string resources to `app/src/main/res/values/strings.xml` for:
      - Quiz question template: "What is the [tense] of [verb]?" (3 variants for past, present, participle)
      - Option labels and button text
      - Verb labels (PRESENT, PAST, PARTICIPLE, PRESENT_PARTICIPLE)
      - Quiz navigation: "Quiz", "Siguiente", "Enviar"
      
      **Files:**
      - `c:\5_semestre\trabajos-finales\VerbPulse\app\src\main\res\values\strings.xml` (modify)
      
      **Verify:**
      ```
      .\gradlew.bat assembleDebug
      ```
      Build must complete. No lint errors for string references.

---

- [ ] **7. Create AppModule.kt in core/di for Hilt dependency bindings.**
      
      Implement a Hilt @Module annotated class that provides Singleton bindings for:
      - UserPreferencesRepository (depends on DataStore context)
      - VerbsRepository (simple singleton)
      - Ensures all repositories are injectable across the app
      
      **Files:**
      - `c:\5_semestre\trabajos-finales\VerbPulse\app\src\main\java\com\verbpulse\app\core\di\AppModule.kt` (create)
      
      **Verify:**
      ```
      .\gradlew.bat assembleDebug
      ```
      Build must complete. Hilt code generation runs without errors.

---

- [ ] **8. Create HiltApplication.kt and update AndroidManifest.xml.**
      
      Create an Application subclass annotated with @HiltAndroidApp to enable Hilt dependency injection at app startup. Update AndroidManifest.xml to reference the new application class.
      
      **Files:**
      - `c:\5_semestre\trabajos-finales\VerbPulse\app\src\main\java\com\verbpulse\app\HiltApplication.kt` (create)
      - `c:\5_semestre\trabajos-finales\VerbPulse\app\src\main\AndroidManifest.xml` (modify)
      
      **Verify:**
      ```
      .\gradlew.bat assembleDebug
      ```
      Build must complete. App runs without Hilt initialization errors.

---

- [ ] **9. Update MainActivity.kt to wire navigation and use UserPreferencesRepository.**
      
      Modify MainActivity.kt to:
      - Add showQuiz Boolean state (alongside existing showFlashcard, isDarkMode, language)
      - Inject UserPreferencesRepository and collect its StateFlow properties
      - Add navigation conditional: Home → Flashcard → Quiz based on state flags
      - Wire "Quiz" button from HomeScreen to set showQuiz = true
      - Wire "Siguiente" button from QuizScreen to reset and return to HomeScreen
      - Pass onStartQuiz callback to FlashcardScreen
      
      **Files:**
      - `c:\5_semestre\trabajos-finales\VerbPulse\app\src\main\java\com\verbpulse\app\presentation\MainActivity.kt` (modify)
      
      **Verify:**
      ```
      .\gradlew.bat assembleDebug
      ```
      Build must complete. Navigation flow works: Home → Flashcard → Quiz → Home (each button navigates correctly).

---

- [ ] **10. Update HomeScreen.kt to add Quiz navigation button.**
      
      Add a "Quiz" button/chip to HomeScreen.kt that:
      - Appears below the theme selection or integrated into the card area
      - Styled consistently (rounded, orange accent, Wear OS aesthetic)
      - Calls onStartQuiz() callback to navigate to QuizScreen
      - Uses stringResource(R.string.home_quiz_button) for text
      
      **Files:**
      - `c:\5_semestre\trabajos-finales\VerbPulse\app\src\main\java\com\verbpulse\app\feature\setup\ui\HomeScreen.kt` (modify)
      
      **Verify:**
      ```
      .\gradlew.bat assembleDebug
      ```
      Build must complete. HomeScreen preview renders with new Quiz button visible.

---

- [ ] **11. Update FlashcardScreen.kt to add Quiz navigation and wire ViewModel.**
      
      Modify FlashcardScreen.kt to:
      - Accept onStartQuiz callback parameter
      - Add a "Quiz" button alongside the existing flip button (or replace flip with Quiz button)
      - Optionally inject FlashcardViewModel and consume currentVerb StateFlow to display dynamic verbs
      - Ensure Preview still renders correctly
      
      **Files:**
      - `c:\5_semestre\trabajos-finales\VerbPulse\app\src\main\java\com\verbpulse\app\feature\learning\ui\FlashcardScreen.kt` (modify)
      
      **Verify:**
      ```
      .\gradlew.bat assembleDebug
      ```
      Build must complete. FlashcardScreen Preview renders with Quiz button.

---

- [ ] **12. Build and verify full app integration.**
      
      Run a clean build and verify:
      - No lint or compile errors
      - All Hilt injections resolve correctly
      - App launches without crashes
      - Navigation flow is functional: Home → Flashcard → Quiz → Home
      
      **Verify:**
      ```
      .\gradlew.bat clean assembleDebug
      ```
      Output must show "BUILD SUCCESSFUL" with no errors or warnings related to new code.

---

- [ ] **13. Commit changes with stage 4 message.**
      
      Create a single commit capturing all Stage 4 work (repositories, ViewModels, QuizScreen, DI setup, navigation wiring).
      
      **Verify:**
      ```
      git -C c:\5_semestre\trabajos-finales\VerbPulse log --oneline -1
      ```
      Output must show: `feat: stage 4 - repository, viewmodels and quiz integrated`

---

## Notes

- **Context Path:** `c:\5_semestre\trabajos-finales\VerbPulse` (workspace root for all relative paths)
- **Build Command:** `.\gradlew.bat assembleDebug`
- **Absolute paths in items use this workspace root as base**
- **String IDs follow pattern:** `home_quiz_button`, `quiz_past_tense_label`, etc.
- **Verb data:** 10 irregular verbs (e.g., Build→built→built, Go→went→gone, See→saw→seen, etc.)
- **No unit tests required for this stage**; end-to-end verification via build and preview rendering
