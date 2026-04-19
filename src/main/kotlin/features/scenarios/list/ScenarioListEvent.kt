package features.scenarios.list

import domain.model.ScenarioPriority

sealed interface ScenarioListEvent {
    data class SearchChanged(val query: String) : ScenarioListEvent
    data class SmokeFilterChanged(val enabled: Boolean) : ScenarioListEvent
    data class PriorityFilterChanged(val priority: ScenarioPriority?) : ScenarioListEvent
    data class TagFilterToggled(val tagId: String) : ScenarioListEvent
    data class SortChanged(val sortOption: ScenarioSortOption) : ScenarioListEvent
    data class ScenarioSelected(val scenarioId: String) : ScenarioListEvent
}
