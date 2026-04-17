package features.runs.list

import domain.repository.EnvironmentRepository
import domain.repository.TestRunRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class RunListViewModel(
    private val testRunRepository: TestRunRepository,
    private val environmentRepository: EnvironmentRepository,
) {
    private val _state = MutableStateFlow(RunListState())
    val state: StateFlow<RunListState> = _state.asStateFlow()

    init {
        reload()
    }

    private fun reload() {
        val runs = testRunRepository.findAll().sortedByDescending { it.createdAt }.map { run ->
            val envName = environmentRepository.findById(run.environmentId)?.name ?: "Unknown"
            RunListItemUi(
                id = run.id,
                name = run.name,
                environmentName = envName,
                status = run.status,
                scenarioCount = run.scenarioIds.size,
                createdAt = run.createdAt.toLocalDateTime(TimeZone.currentSystemDefault()).let {
                    "${it.date} ${it.hour.toString().padStart(2, '0')}:${it.minute.toString().padStart(2, '0')}"
                },
            )
        }
        _state.update { RunListState(runs = runs) }
    }
}
