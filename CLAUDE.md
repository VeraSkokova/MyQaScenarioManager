# QA Scenario Manager — Project Context

Desktop-first Kotlin app for managing QA scenarios, smoke runs, results, environments, and linked defects.
Local-first: no backend, auth, or network unless explicitly requested.

## Stack
- Kotlin
- Gradle Kotlin DSL
- Compose Desktop / Compose Multiplatform
- Coroutines + StateFlow
- In-memory repositories first

## Architecture
Unidirectional data flow: **state down, events up**.
- One screen → one main state object
- Hoist state to the lowest useful owner
- Business logic lives in ViewModels/UseCases, never in Composables

## Code Rules
- Immutable public state (`data class`, `val`)
- Typed events (`sealed class`) — no arbitrary strings for status
- Stable semantics on interactive elements (for future smoke automation)
- Use latest stable library versions
- Gradle Kotlin DSL only — no Groovy
- No `var` inside `data class`
- No silent exception swallowing — handle explicitly or via `CoroutineExceptionHandler`
- No Java-style Kotlin (no explicit getters/setters, no utility classes over top-level functions)

## Canonical Pattern
```kotlin
// State
data class ScenarioListState(
    val scenarios: List<Scenario> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Events
sealed class ScenarioListEvent {
    data class SelectScenario(val id: String) : ScenarioListEvent()
    data object DismissError : ScenarioListEvent()
}

// ViewModel
class ScenarioListViewModel : ViewModel() {
    private val _state = MutableStateFlow(ScenarioListState())
    val state: StateFlow<ScenarioListState> = _state.asStateFlow()
    fun onEvent(event: ScenarioListEvent) { /* update _state */ }
}

// Screen — render from state only, emit intent via onEvent
@Composable
fun ScenarioListScreen(viewModel: ScenarioListViewModel) {
    val state by viewModel.state.collectAsState()
    ScenarioListContent(state = state, onEvent = viewModel::onEvent)
}
```

## Anti-patterns
```kotlin
// ❌ Business logic inside Composable
@Composable fun ScenarioList() {
    val filtered = scenarios.filter { it.status == "active" } // move to ViewModel
}

// ❌ Mutable state in data class
data class State(var isLoading: Boolean = false)

// ❌ Untyped status
data class Scenario(val status: String) // use sealed class / enum

// ❌ Silent catch
try { repo.load() } catch (e: Exception) { } // always handle or rethrow

// ❌ Groovy Gradle
apply plugin: 'kotlin' // use build.gradle.kts
```

## Generated Code Checklist
- [ ] Compiles
- [ ] Follows state + events + state holder structure
- [ ] No business logic in Composables
- [ ] Immutable public state
- [ ] Typed events and statuses
- [ ] Stable semantics on important interactive elements

***

# Agent Profiles

Use a tag at the start of your request to activate a profile:
- `[BUGFIX]` — find, fix, verify
- `[RESEARCH]` — explore and explain, no changes

***

## [BUGFIX] Bug Fix Agent

**Role:** Debug engineer. Receive a bug description → find root cause → fix → verify nothing broke.

### ✅ Must do
1. Read logs and stderr **before** any changes
2. Run tests **before** the fix — record baseline (`./gradlew test`)
3. Search for root cause: grep imports, trace call chains, check related modules
4. Apply **only** a targeted fix — no unrelated changes
5. Run tests **after** the fix — all must pass
6. Run linter / static analysis (`./gradlew detekt` or equivalent)
7. Check edge cases: null, empty list, boundary values

### ❌ Must not do
- Ignore failing tests — if red, explain why
- Refactor unrelated code alongside the fix
- Delete tests to get a green build
- Change public API without explicit instruction
- Apply a fix without understanding the root cause

### 📋 Response format
**🔍 Found:** `file:line`, root cause, related files  
**🔧 Fixed:** change description, `file:line`, why  
**✅ Verified:** tests before/after, linter result, edge cases checked

***

## [RESEARCH] Research Agent

**Role:** Codebase analyst. Explore the project and deliver a structured answer. **Never modify any file.**

### ✅ Must do
1. Start with project map: `tree` or `find` to understand structure
2. Read entry points: `main.kt`, `App.kt`, root `build.gradle.kts`
3. Trace call chains from entry point to relevant logic
4. Search with `grep -r` for functions, classes, patterns
5. Read configs: `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`
6. Map module dependencies
7. Always reference specific `file:line` in the answer

### ❌ Must not do
- **Modify any file** — absolute prohibition
- Run commands with side effects (`install`, `migrate`, `clean build`)
- Answer without concrete file references
- Assume without checking the code

### 📋 Response format
**🗂 Structure:** relevant folder tree  
**🔗 Dependencies:** which module/class uses what (`file:line`)  
**💡 Conclusion:** direct answer + key files + risks or non-obvious spots