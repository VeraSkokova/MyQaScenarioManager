package data

import domain.model.ResultStatus
import domain.model.RunStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SeedDataIntegrityTest {

    @Test
    fun `expected number of scenarios`() {
        assertEquals(10, SeedData.scenarios.size)
    }

    @Test
    fun `expected number of test runs`() {
        assertEquals(3, SeedData.testRuns.size)
    }

    @Test
    fun `expected number of environments`() {
        assertEquals(3, SeedData.environments.size)
    }

    @Test
    fun `expected number of defects`() {
        assertEquals(3, SeedData.defects.size)
    }

    @Test
    fun `expected number of build infos`() {
        assertEquals(2, SeedData.buildInfos.size)
    }

    @Test
    fun `expected number of run results`() {
        assertEquals(14, SeedData.runResults.size)
    }

    @Test
    fun `all scenario IDs are unique`() {
        val ids = SeedData.scenarios.map { it.id }
        assertEquals(ids.size, ids.toSet().size, "Duplicate scenario IDs found")
    }

    @Test
    fun `all run IDs are unique`() {
        val ids = SeedData.testRuns.map { it.id }
        assertEquals(ids.size, ids.toSet().size, "Duplicate run IDs found")
    }

    @Test
    fun `all result IDs are unique`() {
        val ids = SeedData.runResults.map { it.id }
        assertEquals(ids.size, ids.toSet().size, "Duplicate result IDs found")
    }

    @Test
    fun `all run scenario IDs reference existing scenarios`() {
        val scenarioIds = SeedData.scenarios.map { it.id }.toSet()
        SeedData.testRuns.forEach { run ->
            run.scenarioIds.forEach { scenarioId ->
                assertTrue(scenarioId in scenarioIds, "Run '${run.id}' references unknown scenario '$scenarioId'")
            }
        }
    }

    @Test
    fun `all run environment IDs reference existing environments`() {
        val envIds = SeedData.environments.map { it.id }.toSet()
        SeedData.testRuns.forEach { run ->
            assertTrue(run.environmentId in envIds, "Run '${run.id}' references unknown environment '${run.environmentId}'")
        }
    }

    @Test
    fun `all run build info IDs reference existing build infos`() {
        val buildIds = SeedData.buildInfos.map { it.id }.toSet()
        SeedData.testRuns.forEach { run ->
            assertTrue(run.buildInfoId in buildIds, "Run '${run.id}' references unknown build info '${run.buildInfoId}'")
        }
    }

    @Test
    fun `all result run IDs reference existing runs`() {
        val runIds = SeedData.testRuns.map { it.id }.toSet()
        SeedData.runResults.forEach { result ->
            assertTrue(result.runId in runIds, "Result '${result.id}' references unknown run '${result.runId}'")
        }
    }

    @Test
    fun `all result scenario IDs reference existing scenarios`() {
        val scenarioIds = SeedData.scenarios.map { it.id }.toSet()
        SeedData.runResults.forEach { result ->
            assertTrue(result.scenarioId in scenarioIds, "Result '${result.id}' references unknown scenario '${result.scenarioId}'")
        }
    }

    @Test
    fun `all result defect IDs reference existing defects`() {
        val defectIds = SeedData.defects.map { it.id }.toSet()
        SeedData.runResults.forEach { result ->
            result.defectIds.forEach { defectId ->
                assertTrue(defectId in defectIds, "Result '${result.id}' references unknown defect '$defectId'")
            }
        }
    }

    @Test
    fun `all scenario linked defect IDs reference existing defects`() {
        val defectIds = SeedData.defects.map { it.id }.toSet()
        SeedData.scenarios.forEach { scenario ->
            scenario.linkedDefectIds.forEach { defectId ->
                assertTrue(defectId in defectIds, "Scenario '${scenario.id}' references unknown defect '$defectId'")
            }
        }
    }

    @Test
    fun `completed runs have completedAt timestamp`() {
        SeedData.testRuns.filter { it.status == RunStatus.COMPLETED }.forEach { run ->
            assertNotNull(run.completedAt, "Completed run '${run.id}' should have completedAt")
        }
    }

    @Test
    fun `non-completed runs do not have completedAt timestamp`() {
        SeedData.testRuns.filter { it.status != RunStatus.COMPLETED }.forEach { run ->
            assertNull(run.completedAt, "Non-completed run '${run.id}' should not have completedAt")
        }
    }

    @Test
    fun `each run has at least one result per scenario`() {
        SeedData.testRuns.forEach { run ->
            val resultScenarioIds = SeedData.runResults
                .filter { it.runId == run.id }
                .map { it.scenarioId }
                .toSet()
            assertEquals(
                run.scenarioIds.toSet(),
                resultScenarioIds,
                "Run '${run.id}' scenarios and results should match",
            )
        }
    }

    @Test
    fun `draft run results are all NOT_RUN`() {
        val draftRunIds = SeedData.testRuns.filter { it.status == RunStatus.DRAFT }.map { it.id }.toSet()
        SeedData.runResults.filter { it.runId in draftRunIds }.forEach { result ->
            assertEquals(
                ResultStatus.NOT_RUN,
                result.status,
                "Draft run result '${result.id}' should be NOT_RUN",
            )
        }
    }
}
