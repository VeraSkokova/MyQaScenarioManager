package data

import domain.model.ResultStatus
import domain.model.RunScenarioResult
import domain.model.TestRun
import domain.repository.TestRunRepository
import kotlinx.datetime.Clock

class InMemoryTestRunRepository(
    private val runs: List<TestRun>,
    private val results: MutableList<RunScenarioResult>,
) : TestRunRepository {

    override fun findAll(): List<TestRun> = runs

    override fun findById(id: String): TestRun? = runs.find { it.id == id }

    override fun findResultsByRunId(runId: String): List<RunScenarioResult> =
        results.filter { it.runId == runId }

    override fun findAllResults(): List<RunScenarioResult> = results.toList()

    override fun updateResultStatus(
        resultId: String,
        status: ResultStatus,
        comment: String,
    ): RunScenarioResult? {
        val index = results.indexOfFirst { it.id == resultId }
        if (index == -1) return null
        val updated = results[index].copy(
            status = status,
            comment = comment,
            executedAt = Clock.System.now(),
        )
        results[index] = updated
        return updated
    }

    override fun getRecentRuns(limit: Int): List<TestRun> =
        runs.sortedByDescending { it.createdAt }.take(limit)
}
