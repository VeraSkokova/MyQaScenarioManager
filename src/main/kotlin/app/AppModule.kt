package app

import data.*
import domain.repository.*
import features.dashboard.DashboardViewModel
import features.runs.details.RunDetailsViewModel
import features.runs.list.RunListViewModel
import features.scenarios.details.ScenarioDetailsViewModel
import features.scenarios.list.ScenarioListViewModel

object AppModule {

    val scenarioRepository: ScenarioRepository = InMemoryScenarioRepository(SeedData.scenarios)
    val testRunRepository: TestRunRepository = InMemoryTestRunRepository(SeedData.testRuns, SeedData.runResults.toMutableList())
    val environmentRepository: EnvironmentRepository = InMemoryEnvironmentRepository(SeedData.environments)
    val defectRepository: DefectRepository = InMemoryDefectRepository(SeedData.defects)
    val buildInfoRepository: BuildInfoRepository = InMemoryBuildInfoRepository(SeedData.buildInfos)
    val scenarioJsonService: ScenarioJsonService = ScenarioJsonService(scenarioRepository)

    fun dashboardViewModel() = DashboardViewModel(scenarioRepository, testRunRepository, environmentRepository)

    fun scenarioListViewModel() = ScenarioListViewModel(scenarioRepository, scenarioJsonService)

    fun scenarioDetailsViewModel(scenarioId: String) = ScenarioDetailsViewModel(scenarioRepository, defectRepository, scenarioId)

    fun runListViewModel() = RunListViewModel(testRunRepository, environmentRepository)

    fun runDetailsViewModel(runId: String) = RunDetailsViewModel(testRunRepository, scenarioRepository, environmentRepository, buildInfoRepository, runId)
}
