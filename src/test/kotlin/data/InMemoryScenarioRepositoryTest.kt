package data

import domain.model.Scenario
import domain.model.ScenarioPriority
import domain.model.ScenarioType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class InMemoryScenarioRepositoryTest {

    private val scenarios = listOf(
        scenario("1", "Login Flow", "User logs in with valid credentials", ScenarioType.SMOKE),
        scenario("2", "Checkout Process", "Full checkout with payment", ScenarioType.FUNCTIONAL),
        scenario("3", "Smoke Dashboard", "Verify dashboard loads", ScenarioType.SMOKE),
        scenario("4", "Search Products", "Search by name and filter", ScenarioType.FUNCTIONAL),
    )

    private val repo = InMemoryScenarioRepository(scenarios)

    @Test
    fun `findAll returns all scenarios`() {
        assertEquals(4, repo.findAll().size)
    }

    @Test
    fun `findById returns matching scenario`() {
        val result = repo.findById("2")
        assertEquals("Checkout Process", result?.title)
    }

    @Test
    fun `findById returns null for unknown id`() {
        assertNull(repo.findById("nonexistent"))
    }

    @Test
    fun `findScenarios with blank query and smokeOnly false returns all`() {
        val result = repo.findScenarios("", smokeOnly = false)
        assertEquals(4, result.size)
    }

    @Test
    fun `findScenarios filters by query in title`() {
        val result = repo.findScenarios("login", smokeOnly = false)
        assertEquals(1, result.size)
        assertEquals("1", result.first().id)
    }

    @Test
    fun `findScenarios filters by query in description`() {
        val result = repo.findScenarios("payment", smokeOnly = false)
        assertEquals(1, result.size)
        assertEquals("2", result.first().id)
    }

    @Test
    fun `findScenarios query is case insensitive`() {
        val result = repo.findScenarios("LOGIN", smokeOnly = false)
        assertEquals(1, result.size)
    }

    @Test
    fun `findScenarios with smokeOnly true returns only smoke scenarios`() {
        val result = repo.findScenarios("", smokeOnly = true)
        assertEquals(2, result.size)
        result.forEach { assertEquals(ScenarioType.SMOKE, it.type) }
    }

    @Test
    fun `findScenarios combines query and smokeOnly filters`() {
        val result = repo.findScenarios("dashboard", smokeOnly = true)
        assertEquals(1, result.size)
        assertEquals("3", result.first().id)
    }

    @Test
    fun `findScenarios with query matching non-smoke and smokeOnly true returns empty`() {
        val result = repo.findScenarios("checkout", smokeOnly = true)
        assertEquals(0, result.size)
    }

    @Test
    fun `countByType counts smoke scenarios`() {
        assertEquals(2, repo.countByType(ScenarioType.SMOKE))
    }

    @Test
    fun `countByType counts functional scenarios`() {
        assertEquals(2, repo.countByType(ScenarioType.FUNCTIONAL))
    }

    @Test
    fun `countByType returns zero for empty repo`() {
        val emptyRepo = InMemoryScenarioRepository(emptyList())
        assertEquals(0, emptyRepo.countByType(ScenarioType.SMOKE))
    }

    private fun scenario(
        id: String,
        title: String,
        description: String,
        type: ScenarioType,
    ) = Scenario(
        id = id,
        title = title,
        description = description,
        type = type,
        priority = ScenarioPriority.MEDIUM,
        tags = emptyList(),
        steps = emptyList(),
        expectedResult = "Success",
    )
}
