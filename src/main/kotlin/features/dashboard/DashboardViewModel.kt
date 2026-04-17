package features.dashboard

import domain.model.ResultStatus
import domain.model.ScenarioType
import domain.repository.EnvironmentRepository
import domain.repository.ScenarioRepository
import domain.repository.TestRunRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DashboardViewModel(
    private val scenarioRepository: ScenarioRepository,
    private val testRunRepository: TestRunRepository,
    private val environmentRepository: EnvironmentRepository,
) {
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        reload()
    }

    private fun reload() {
        val allResults = testRunRepository.findAllResults()
        val executedResults = allResults.filter { it.status != ResultStatus.NOT_RUN }
        val passRate = if (executedResults.isNotEmpty()) {
            executedResults.count { it.status == ResultStatus.PASSED }.toFloat() / executedResults.size * 100f
        } else {
            0f
        }

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val failedToday = allResults.count { result ->
            result.status == ResultStatus.FAILED && result.executedAt?.let {
                it.toLocalDateTime(TimeZone.currentSystemDefault()).date == today
            } == true
        }

        val recentRuns = testRunRepository.getRecentRuns(5).map { run ->
            val envName = environmentRepository.findById(run.environmentId)?.name ?: "Unknown"
            RecentRunUi(
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

        _state.update {
            DashboardState(
                passRatePercent = passRate,
                failedTodayCount = failedToday,
                smokeScenariosTotal = scenarioRepository.countByType(ScenarioType.SMOKE),
                totalScenarios = scenarioRepository.findAll().size,
                recentRuns = recentRuns,
            )
        }
    }
}
