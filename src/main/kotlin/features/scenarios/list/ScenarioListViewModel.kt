package features.scenarios.list

import domain.repository.ScenarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScenarioListViewModel(
    private val scenarioRepository: ScenarioRepository,
) {
    private val _state = MutableStateFlow(ScenarioListState())
    val state: StateFlow<ScenarioListState> = _state.asStateFlow()

    init {
        loadAvailableTags()
        reload()
    }

    fun onEvent(event: ScenarioListEvent) {
        when (event) {
            is ScenarioListEvent.SearchChanged -> {
                _state.update { it.copy(searchQuery = event.query) }
                reload()
            }
            is ScenarioListEvent.SmokeFilterChanged -> {
                _state.update { it.copy(smokeOnly = event.enabled) }
                reload()
            }
            is ScenarioListEvent.PriorityFilterChanged -> {
                _state.update { it.copy(priorityFilter = event.priority) }
                reload()
            }
            is ScenarioListEvent.TagFilterToggled -> {
                _state.update { state ->
                    val newTags = if (event.tagId in state.selectedTagIds) {
                        state.selectedTagIds - event.tagId
                    } else {
                        state.selectedTagIds + event.tagId
                    }
                    state.copy(selectedTagIds = newTags)
                }
                reload()
            }
            is ScenarioListEvent.SortChanged -> {
                _state.update { it.copy(sortOption = event.sortOption) }
                reload()
            }
            is ScenarioListEvent.ScenarioSelected -> {
                // handled at screen level via callback
            }
        }
    }

    private fun loadAvailableTags() {
        val all = scenarioRepository.findAll()
        val allTags = all
            .flatMap { it.tags }
            .distinctBy { it.id }
            .sortedBy { it.name }
        _state.update { it.copy(availableTags = allTags, totalCount = all.size) }
    }

    private fun reload() {
        val current = _state.value
        val filtered = scenarioRepository.findScenarios(
            query = current.searchQuery,
            smokeOnly = current.smokeOnly,
            priority = current.priorityFilter,
            tagIds = current.selectedTagIds,
        )
        val sorted = when (current.sortOption) {
            ScenarioSortOption.TITLE_ASC -> filtered.sortedBy { it.title.lowercase() }
            ScenarioSortOption.TITLE_DESC -> filtered.sortedByDescending { it.title.lowercase() }
            ScenarioSortOption.PRIORITY_ASC -> filtered.sortedBy { it.priority.ordinal }
            ScenarioSortOption.PRIORITY_DESC -> filtered.sortedByDescending { it.priority.ordinal }
        }
        val scenarios = sorted.map { scenario ->
            ScenarioListItemUi(
                id = scenario.id,
                title = scenario.title,
                type = scenario.type,
                priority = scenario.priority,
                tagNames = scenario.tags.map { it.name },
                stepCount = scenario.steps.size,
            )
        }
        _state.update { it.copy(scenarios = scenarios) }
    }
}
