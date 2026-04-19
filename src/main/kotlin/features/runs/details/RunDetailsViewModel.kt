package features.runs.details

import domain.model.ResultStatus
import domain.repository.BuildInfoRepository
import domain.repository.EnvironmentRepository
import domain.repository.ScenarioRepository
import domain.repository.TestRunRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class RunDetailsViewModel(
    private val testRunRepository: TestRunRepository,
    private val scenarioRepository: ScenarioRepository,
    private val environmentRepository: EnvironmentRepository,
    private val buildInfoRepository: BuildInfoRepository,
    private val runId: String,
) {
    private val _state = MutableStateFlow(RunDetailsState())
    val state: StateFlow<RunDetailsState> = _state.asStateFlow()

    init {
        load()
    }

    fun onEvent(event: RunDetailsEvent) {
        when (event) {
            is RunDetailsEvent.BackClicked -> {
                // handled at screen level
            }
            is RunDetailsEvent.StatusChanged -> {
                val existingComment = _state.value.results
                    .find { it.resultId == event.resultId }?.comment.orEmpty()
                testRunRepository.updateResultStatus(event.resultId, event.newStatus, existingComment)
                load()
            }
        }
    }

    private fun load() {
        val run = testRunRepository.findById(runId) ?: return
        val env = environmentRepository.findById(run.environmentId)
        val build = buildInfoRepository.findById(run.buildInfoId)
        val results = testRunRepository.findResultsByRunId(runId)

        val tz = TimeZone.currentSystemDefault()

        _state.update {
            RunDetailsState(
                run = RunDetailUi(
                    id = run.id,
                    name = run.name,
                    environmentName = env?.name ?: "Unknown",
                    buildVersion = build?.version ?: "Unknown",
                    buildBranch = build?.branch ?: "Unknown",
                    status = run.status,
                    createdAt = run.createdAt.toLocalDateTime(tz).let { ldt ->
                        "${ldt.date} ${ldt.hour.toString().padStart(2, '0')}:${ldt.minute.toString().padStart(2, '0')}"
                    },
                    completedAt = run.completedAt?.toLocalDateTime(tz)?.let { ldt ->
                        "${ldt.date} ${ldt.hour.toString().padStart(2, '0')}:${ldt.minute.toString().padStart(2, '0')}"
                    },
                ),
                results = results.map { result ->
                    val scenarioTitle = scenarioRepository.findById(result.scenarioId)?.title ?: "Unknown scenario"
                    RunResultRowUi(
                        resultId = result.id,
                        scenarioTitle = scenarioTitle,
                        status = result.status,
                        comment = result.comment,
                        executedAt = result.executedAt?.toLocalDateTime(tz)?.let { ldt ->
                            "${ldt.date} ${ldt.hour.toString().padStart(2, '0')}:${ldt.minute.toString().padStart(2, '0')}"
                        },
                    )
                },
            )
        }
    }
}
