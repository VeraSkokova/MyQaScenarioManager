# Architecture Decisions — QA Scenario Manager

## 1. Unidirectional Data Flow (UDF)

**Decision:** All screens follow state-down, events-up pattern.

**Rationale:** UDF makes state changes predictable and traceable. Each screen has a single state object flowing down via `StateFlow`, and user interactions are modeled as typed events flowing up through `onEvent()`. This eliminates the class of bugs where multiple sources mutate state independently.

**Implementation:**
- `data class *State` — immutable snapshot of everything the UI needs to render
- `sealed interface *Event` — typed user interactions (no arbitrary strings)
- ViewModel holds `MutableStateFlow<State>` privately, exposes `StateFlow<State>` publicly
- Composables are pure functions of state: `State -> UI`

## 2. ViewModel as State Holder

**Decision:** Each screen has a dedicated ViewModel that owns business logic and state.

**Rationale:** Composables must remain free of business logic to stay testable and reusable. The ViewModel serves as the single orchestrator for a screen — it queries repositories, transforms data into UI models, and updates state in response to events.

**Pattern:**
```kotlin
class FooViewModel(private val repo: FooRepository) {
    private val _state = MutableStateFlow(FooState())
    val state: StateFlow<FooState> = _state.asStateFlow()

    fun onEvent(event: FooEvent) { /* update _state */ }
}
```

## 3. In-Memory Repositories

**Decision:** All data is stored in-memory using simple Kotlin collections. No database, no network.

**Rationale:** The application is local-first with no backend requirements. In-memory storage provides the fastest iteration cycle and zero infrastructure dependencies. The repository interface layer allows swapping in persistent storage (SQLite, file-based) later without changing any ViewModel or UI code.

**Trade-offs:**
- Data is lost on application restart (acceptable for current MVP scope)
- Seed data provides a consistent starting state for development and testing
- Repository interfaces are defined in `domain/repository/`, implementations in `data/`

## 4. Typed Statuses and Priorities

**Decision:** All status and category fields use enums or sealed classes, never raw strings.

**Rationale:** String-based statuses are a common source of bugs — typos, inconsistent casing, missing cases in `when` expressions. Kotlin enums provide compile-time exhaustiveness checking and eliminate an entire class of runtime errors.

**Examples:**
- `ResultStatus` — PASSED, FAILED, BLOCKED, NOT_RUN
- `RunStatus` — DRAFT, IN_PROGRESS, COMPLETED
- `DefectStatus` — OPEN, IN_PROGRESS, FIXED, CLOSED, WONT_FIX
- `ScenarioPriority` — LOW, MEDIUM, HIGH, CRITICAL
- `ScenarioType` — SMOKE, FUNCTIONAL

## 5. Compose Desktop with Material 3

**Decision:** UI built with Jetpack Compose for Desktop using Material 3 design system.

**Rationale:** Compose provides a declarative UI framework that aligns naturally with unidirectional data flow. Material 3 provides a consistent, accessible design language out of the box. The same composable patterns work across Desktop and Android if multiplatform expansion is needed.

## 6. Navigation with Back Stack

**Decision:** Custom navigation using `NavigationState` with a manual back stack.

**Rationale:** Compose Desktop does not ship with a built-in navigation library like Android's Navigation component. A simple stack-based approach with `navigateTo()` (root) and `navigateWithBackStack()` (push) provides adequate navigation for the current screen count while remaining easy to understand and modify.

## 7. Centralized Dependency Injection via AppModule

**Decision:** A single `AppModule` object provides factory functions for all ViewModels and singleton repository instances.

**Rationale:** The application is small enough that a DI framework (Koin, Dagger) would add unnecessary complexity. `AppModule` serves as a manual service locator — all dependencies are explicit, constructors are simple, and the wiring is visible in one file. This can be replaced with a DI framework if the project grows.
