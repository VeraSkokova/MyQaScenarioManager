package features.scenarios.list

import data.ScenarioJsonService
import domain.repository.ScenarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScenarioListViewModel(
    private val scenarioRepository: ScenarioRepository,
    private val scenarioJsonService: ScenarioJsonService,
) {
    private val _state = MutableStateFlow(ScenarioListState())
    val state: StateFlow<ScenarioListState> = _state.asStateFlow()

    init {
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
            is ScenarioListEvent.ScenarioSelected -> {
                // handled at screen level via callback
            }
            is ScenarioListEvent.ExportScenarios -> exportScenarios(event)
            is ScenarioListEvent.ImportScenarios -> importScenarios(event)
            is ScenarioListEvent.DismissMessage -> {
                _state.update { it.copy(userMessage = null) }
            }
        }
    }

    private fun exportScenarios(event: ScenarioListEvent.ExportScenarios) {
        try {
            scenarioJsonService.exportToFile(event.path)
            val count = scenarioRepository.findAll().size
            _state.update { it.copy(userMessage = "Exported $count scenarios to ${event.path.fileName}") }
        } catch (e: Exception) {
            _state.update { it.copy(userMessage = "Export failed: ${e.message}") }
        }
    }

    private fun importScenarios(event: ScenarioListEvent.ImportScenarios) {
        try {
            val imported = scenarioJsonService.importFromFile(event.path)
            _state.update { it.copy(userMessage = "Imported $imported new scenarios from ${event.path.fileName}") }
            reload()
        } catch (e: Exception) {
            _state.update { it.copy(userMessage = "Import failed: ${e.message}") }
        }
    }

    private fun reload() {
        val current = _state.value
        val scenarios = scenarioRepository.findScenarios(
            query = current.searchQuery,
            smokeOnly = current.smokeOnly,
        ).map { scenario ->
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
