package domain.repository

import domain.model.ResultStatus
import domain.model.RunScenarioResult
import domain.model.TestRun

interface TestRunRepository {
    fun findAll(): List<TestRun>
    fun findById(id: String): TestRun?
    fun findResultsByRunId(runId: String): List<RunScenarioResult>
    fun findAllResults(): List<RunScenarioResult>
    fun updateResultStatus(resultId: String, status: ResultStatus, comment: String): RunScenarioResult?
    fun getRecentRuns(limit: Int = 5): List<TestRun>
}
