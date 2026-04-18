package features.scenarios.list

import java.nio.file.Path

sealed interface ScenarioListEvent {
    data class SearchChanged(val query: String) : ScenarioListEvent
    data class SmokeFilterChanged(val enabled: Boolean) : ScenarioListEvent
    data class ScenarioSelected(val scenarioId: String) : ScenarioListEvent
    data class ExportScenarios(val path: Path) : ScenarioListEvent
    data class ImportScenarios(val path: Path) : ScenarioListEvent
    data object DismissMessage : ScenarioListEvent
}
