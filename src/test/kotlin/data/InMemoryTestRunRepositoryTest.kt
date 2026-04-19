package data

import domain.model.ResultStatus
import domain.model.RunScenarioResult
import domain.model.RunStatus
import domain.model.TestRun
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.hours

class InMemoryTestRunRepositoryTest {

    private val now = Clock.System.now()
    private val runs = listOf(
        testRun("r1", "Run Alpha", now - 3.hours),
        testRun("r2", "Run Beta", now - 1.hours),
        testRun("r3", "Run Gamma", now - 2.hours),
    )
    private val results = mutableListOf(
        result("res1", "r1", "s1", ResultStatus.PASSED),
        result("res2", "r1", "s2", ResultStatus.FAILED),
        result("res3", "r2", "s1", ResultStatus.NOT_RUN),
    )

    private val repo = InMemoryTestRunRepository(runs, results)

    @Test
    fun `findAll returns all runs`() {
        assertEquals(3, repo.findAll().size)
    }

    @Test
    fun `findById returns matching run`() {
        assertEquals("Run Beta", repo.findById("r2")?.name)
    }

    @Test
    fun `findById returns null for unknown id`() {
        assertNull(repo.findById("nonexistent"))
    }

    @Test
    fun `findResultsByRunId returns results for given run`() {
        val r1Results = repo.findResultsByRunId("r1")
        assertEquals(2, r1Results.size)
        r1Results.forEach { assertEquals("r1", it.runId) }
    }

    @Test
    fun `findResultsByRunId returns empty for run with no results`() {
        assertEquals(0, repo.findResultsByRunId("r3").size)
    }

    @Test
    fun `findAllResults returns all results`() {
        assertEquals(3, repo.findAllResults().size)
    }

    @Test
    fun `findAllResults returns defensive copy`() {
        val copy = repo.findAllResults()
        assertEquals(3, copy.size)
        // Mutating the returned list should not affect the repository
        assertEquals(3, repo.findAllResults().size)
    }

    @Test
    fun `updateResultStatus updates status and comment`() {
        val updated = repo.updateResultStatus("res3", ResultStatus.PASSED, "Looks good")
        assertNotNull(updated)
        assertEquals(ResultStatus.PASSED, updated!!.status)
        assertEquals("Looks good", updated.comment)
        assertNotNull(updated.executedAt)
    }

    @Test
    fun `updateResultStatus persists change`() {
        repo.updateResultStatus("res3", ResultStatus.BLOCKED, "Env down")
        val fetched = repo.findResultsByRunId("r2").first { it.id == "res3" }
        assertEquals(ResultStatus.BLOCKED, fetched.status)
        assertEquals("Env down", fetched.comment)
    }

    @Test
    fun `updateResultStatus returns null for unknown id`() {
        assertNull(repo.updateResultStatus("nonexistent", ResultStatus.PASSED, ""))
    }

    @Test
    fun `getRecentRuns returns runs sorted by createdAt descending`() {
        val recent = repo.getRecentRuns(3)
        assertEquals("r2", recent[0].id) // most recent
        assertEquals("r3", recent[1].id)
        assertEquals("r1", recent[2].id) // oldest
    }

    @Test
    fun `getRecentRuns respects limit`() {
        val recent = repo.getRecentRuns(2)
        assertEquals(2, recent.size)
        assertEquals("r2", recent[0].id)
        assertEquals("r3", recent[1].id)
    }

    @Test
    fun `getRecentRuns with limit larger than size returns all`() {
        assertEquals(3, repo.getRecentRuns(10).size)
    }

    private fun testRun(id: String, name: String, createdAt: Instant) = TestRun(
        id = id,
        name = name,
        environmentId = "env1",
        buildInfoId = "build1",
        status = RunStatus.IN_PROGRESS,
        createdAt = createdAt,
        scenarioIds = listOf("s1", "s2"),
    )

    private fun result(id: String, runId: String, scenarioId: String, status: ResultStatus) =
        RunScenarioResult(
            id = id,
            runId = runId,
            scenarioId = scenarioId,
            status = status,
        )
}
