# CLAUDE.md

## Project

QA Scenario Manager is a desktop-first Kotlin app for managing QA scenarios, smoke runs, results, environments, and linked defects. Build for Compose Desktop first. Keep the project local-first: no backend, auth, or network unless explicitly requested.[1][2][3]

## Stack

- Kotlin
- Gradle Kotlin DSL
- Compose Desktop / Compose Multiplatform
- Coroutines
- StateFlow
- In-memory repositories first

## Core rules

- Use unidirectional data flow: state down, events up.[4][5]
- One screen = one main state object.[4]
- Hoist state to the lowest useful owner.[6]
- Keep composables mostly stateless.[4][6]
- Business logic belongs in state holders, use cases, or repositories, not composables.[4][5]
- Expose immutable state; keep mutable flow private.[4]
- Prefer immutable `data class` models and typed enums/sealed interfaces.[7][8]
- Use stable semantics/test hooks on important UI controls for future smoke automation.[9][10]

## Package structure

```text
app/
core/
domain/
data/
features/
```

Typical screen files:
- `XxxScreen.kt`
- `XxxState.kt`
- `XxxEvent.kt`
- `XxxViewModel.kt`

## Domain model

Core entities:
- `Scenario`
- `ScenarioStep`
- `TestRun`
- `RunScenarioResult`
- `Environment`
- `BuildInfo`
- `Defect`
- `Tag`

Core enums:
- `ScenarioType { SMOKE, FUNCTIONAL }`
- `ScenarioPriority { LOW, MEDIUM, HIGH, CRITICAL }`
- `RunStatus { DRAFT, IN_PROGRESS, COMPLETED }`
- `ResultStatus { PASSED, FAILED, BLOCKED, NOT_RUN }`

## Naming

- Screens: `ScenarioListScreen`, `RunDetailsScreen`
- State: `ScenarioListState`
- Events: `ScenarioListEvent`
- State holders: `ScenarioListViewModel`
- Repositories: `ScenarioRepository`, `InMemoryScenarioRepository`
- Use cases: `CreateRunUseCase`

Prefer domain names over generic names. Example: `markScenarioFailed()` is better than `handleClick()`.

## Good patterns

### Immutable model

```kotlin
data class Scenario(
    val id: String,
    val title: String,
    val type: ScenarioType,
    val priority: ScenarioPriority,
    val steps: List<ScenarioStep>,
    val expectedResult: String,
    val linkedDefectIds: List<String> = emptyList()
)
```

### Typed events

```kotlin
sealed interface ScenarioListEvent {
    data class SearchChanged(val query: String) : ScenarioListEvent
    data class SmokeFilterChanged(val enabled: Boolean) : ScenarioListEvent
    data class ScenarioSelected(val scenarioId: String) : ScenarioListEvent
    data object CreateScenarioClicked : ScenarioListEvent
}
```

### State holder

```kotlin
class ScenarioListViewModel(
    private val scenarioRepository: ScenarioRepository
) {
    private val _state = MutableStateFlow(ScenarioListState())
    val state: StateFlow<ScenarioListState> = _state.asStateFlow()

    fun onEvent(event: ScenarioListEvent) {
        when (event) {
            is ScenarioListEvent.SearchChanged -> _state.update { it.copy(searchQuery = event.query) }
            is ScenarioListEvent.SmokeFilterChanged -> _state.update { it.copy(smokeOnly = event.enabled) }
            is ScenarioListEvent.ScenarioSelected -> _state.update { it.copy(selectedScenarioId = event.scenarioId) }
            ScenarioListEvent.CreateScenarioClicked -> Unit
        }
    }
}
```

### Stateless UI

```kotlin
@Composable
fun ScenarioListToolbar(
    searchQuery: String,
    smokeOnly: Boolean,
    onSearchChange: (String) -> Unit,
    onSmokeOnlyChange: (Boolean) -> Unit,
    onCreateClick: () -> Unit
) {
    Button(
        onClick = onCreateClick,
        modifier = Modifier.semantics { testTag = "create_scenario_button" }
    ) {
        Text("New scenario")
    }
}
```

## Anti-patterns

- Business logic or repository access inside composables.[4][5]
- Global mutable UI state.
- Stringly-typed statuses like `"failed"` instead of enums.[7][8]
- Huge screen files mixing UI, fake data, mapping, and logic.
- Ambiguous automation hooks, for example several buttons all labeled `Save`.[9]

Bad example:

```kotlin
@Composable
fun ScenarioListScreen(repository: ScenarioRepository) {
    val scenarios = repository.findAll().filter { it.type.name == "SMOKE" }
}
```

## Typical file templates

### State

```kotlin
data class ScenarioListState(
    val searchQuery: String = "",
    val smokeOnly: Boolean = false,
    val selectedScenarioId: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
```

### Event

```kotlin
sealed interface ScenarioListEvent {
    data class SearchChanged(val query: String) : ScenarioListEvent
    data class SmokeFilterChanged(val enabled: Boolean) : ScenarioListEvent
    data class ScenarioSelected(val scenarioId: String) : ScenarioListEvent
    data object CreateScenarioClicked : ScenarioListEvent
    data object DismissError : ScenarioListEvent
}
```

### Screen

```kotlin
@Composable
fun ScenarioListScreen(viewModel: ScenarioListViewModel) {
    val state by viewModel.state.collectAsState()
    ScenarioListContent(state = state, onEvent = viewModel::onEvent)
}
```

## Definition of done

Generated code should:
- compile;
- follow state + events + state holder structure;
- keep business logic out of composables;[4][5]
- use immutable public state;[4]
- use typed statuses/events;[7][8]
- include stable semantics for important interactive elements.[9][10]

## Agent behavior

When implementing a feature:
1. Extend domain only if the business concept is real.
2. Update `State`, `Event`, and `ViewModel/StateHolder` first.
3. Keep changes local and incremental.
4. Reuse existing patterns before inventing new ones.
5. Do not add DB, DI, networking, or major libraries without explicit request.

If existing code conflicts with this file, prefer the real project style unless it clearly harms readability, correctness, or testability.
