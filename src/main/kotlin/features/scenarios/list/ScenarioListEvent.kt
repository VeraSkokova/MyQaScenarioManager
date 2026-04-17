package features.scenarios.list

sealed interface ScenarioListEvent {
    data class SearchChanged(val query: String) : ScenarioListEvent
    data class SmokeFilterChanged(val enabled: Boolean) : ScenarioListEvent
    data class ScenarioSelected(val scenarioId: String) : ScenarioListEvent
}
