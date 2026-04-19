package features.dashboard

import data.InMemoryEnvironmentRepository
import data.InMemoryScenarioRepository
import data.InMemoryTestRunRepository
import domain.model.Environment
import domain.model.ResultStatus
import domain.model.RunScenarioResult
import domain.model.RunStatus
import domain.model.Scenario
import domain.model.ScenarioPriority
import domain.model.ScenarioType
import domain.model.TestRun
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.time.Duration.Companion.hours

class DashboardViewModelTest {

    private val now = Clock.System.now()

    @Test
    fun `pass rate is calculated from executed results`() {
        val vm = createViewModel(
            results = mutableListOf(
                result("1", "r1", ResultStatus.PASSED),
                result("2", "r1", ResultStatus.PASSED),
                result("3", "r1", ResultStatus.FAILED),
                result("4", "r1", ResultStatus.NOT_RUN), // excluded from calculation
            ),
        )
        // 2 passed / 3 executed = 66.67%
        val passRate = vm.state.value.passRatePercent
        assert(abs(passRate - 66.67f) < 1f) { "Expected ~66.67% but was $passRate" }
    }

    @Test
    fun `pass rate is zero when no executed results`() {
        val vm = createViewModel(
            results = mutableListOf(
                result("1", "r1", ResultStatus.NOT_RUN),
                result("2", "r1", ResultStatus.NOT_RUN),
            ),
        )
        assertEquals(0f, vm.state.value.passRatePercent)
    }

    @Test
    fun `pass rate is zero when no results at all`() {
        val vm = createViewModel(results = mutableListOf())
        assertEquals(0f, vm.state.value.passRatePercent)
    }

    @Test
    fun `failed today count reflects results that failed today`() {
        val vm = createViewModel(
            results = mutableListOf(
                result("1", "r1", ResultStatus.FAILED, executedAt = now),                // today
                result("2", "r1", ResultStatus.FAILED, executedAt = now - 48.hours),     // not today
                result("3", "r1", ResultStatus.PASSED, executedAt = now),                // passed, not failed
                result("4", "r1", ResultStatus.FAILED),                                  // no executedAt
            ),
        )
        assertEquals(1, vm.state.value.failedTodayCount)
    }

    @Test
    fun `total scenarios and smoke counts are correct`() {
        val scenarios = listOf(
            scenario("s1", ScenarioType.SMOKE),
            scenario("s2", ScenarioType.FUNCTIONAL),
            scenario("s3", ScenarioType.SMOKE),
        )
        val vm = createViewModel(scenarios = scenarios)
        assertEquals(3, vm.state.value.totalScenarios)
        assertEquals(2, vm.state.value.smokeScenariosTotal)
    }

    @Test
    fun `recent runs are mapped with environment name`() {
        val vm = createViewModel(
            runs = listOf(
                testRun("r1", "Regression Run", "env1", now - 1.hours),
            ),
            environments = listOf(
                Environment("env1", "Staging", "https://staging.example.com"),
            ),
        )
        val recentRuns = vm.state.value.recentRuns
        assertEquals(1, recentRuns.size)
        assertEquals("Regression Run", recentRuns[0].name)
        assertEquals("Staging", recentRuns[0].environmentName)
    }

    @Test
    fun `recent runs show Unknown for missing environment`() {
        val vm = createViewModel(
            runs = listOf(
                testRun("r1", "Run", "missing-env", now),
            ),
            environments = emptyList(),
        )
        assertEquals("Unknown", vm.state.value.recentRuns[0].environmentName)
    }

    @Test
    fun `recent runs include scenario count`() {
        val run = TestRun(
            id = "r1",
            name = "Run",
            environmentId = "env1",
            buildInfoId = "b1",
            status = RunStatus.IN_PROGRESS,
            createdAt = now,
            scenarioIds = listOf("s1", "s2", "s3"),
        )
        val vm = createViewModel(runs = listOf(run))
        assertEquals(3, vm.state.value.recentRuns[0].scenarioCount)
    }

    private fun createViewModel(
        scenarios: List<Scenario> = listOf(scenario("s1", ScenarioType.FUNCTIONAL)),
        runs: List<TestRun> = listOf(testRun("r1", "Default Run", "env1", now)),
        results: MutableList<RunScenarioResult> = mutableListOf(),
        environments: List<Environment> = listOf(Environment("env1", "Dev", "http://dev")),
    ): DashboardViewModel {
        return DashboardViewModel(
            scenarioRepository = InMemoryScenarioRepository(scenarios),
            testRunRepository = InMemoryTestRunRepository(runs, results),
            environmentRepository = InMemoryEnvironmentRepository(environments),
        )
    }

    private fun scenario(id: String, type: ScenarioType) = Scenario(
        id = id,
        title = "Scenario $id",
        description = "",
        type = type,
        priority = ScenarioPriority.MEDIUM,
        tags = emptyList(),
        steps = emptyList(),
        expectedResult = "",
    )

    private fun testRun(
        id: String,
        name: String,
        envId: String,
        createdAt: kotlinx.datetime.Instant,
    ) = TestRun(
        id = id,
        name = name,
        environmentId = envId,
        buildInfoId = "b1",
        status = RunStatus.IN_PROGRESS,
        createdAt = createdAt,
        scenarioIds = listOf("s1"),
    )

    private fun result(
        id: String,
        runId: String,
        status: ResultStatus,
        executedAt: kotlinx.datetime.Instant? = null,
    ) = RunScenarioResult(
        id = id,
        runId = runId,
        scenarioId = "s1",
        status = status,
        executedAt = executedAt,
    )
}
