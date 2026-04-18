package data

import domain.model.*
import domain.repository.ScenarioRepository
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.readText
import kotlin.test.*

class ScenarioJsonServiceTest {

    private lateinit var repository: ScenarioRepository
    private lateinit var service: ScenarioJsonService

    private val sampleScenarios = listOf(
        Scenario(
            id = "sc-1",
            title = "Login with valid credentials",
            description = "Verify login works",
            type = ScenarioType.SMOKE,
            priority = ScenarioPriority.CRITICAL,
            tags = listOf(Tag("t-1", "auth")),
            steps = listOf(
                ScenarioStep("st-1", 1, "Open login page", "Login page is displayed"),
                ScenarioStep("st-2", 2, "Enter credentials", "Credentials accepted"),
            ),
            expectedResult = "User is logged in",
        ),
        Scenario(
            id = "sc-2",
            title = "Search products by name",
            description = "Verify search functionality",
            type = ScenarioType.FUNCTIONAL,
            priority = ScenarioPriority.MEDIUM,
            tags = listOf(Tag("t-2", "search")),
            steps = listOf(
                ScenarioStep("st-3", 1, "Type product name", "Suggestions appear"),
            ),
            expectedResult = "Matching products are listed",
        ),
    )

    @BeforeTest
    fun setUp() {
        repository = InMemoryScenarioRepository(sampleScenarios)
        service = ScenarioJsonService(repository)
    }

    @Test
    fun `export writes all scenarios as JSON`() {
        val tempFile = createTempFile(suffix = ".json")
        try {
            service.exportToFile(tempFile)
            val content = tempFile.readText()
            assertTrue(content.contains("Login with valid credentials"))
            assertTrue(content.contains("Search products by name"))
            assertTrue(content.contains("SMOKE"))
            assertTrue(content.contains("CRITICAL"))
        } finally {
            tempFile.deleteIfExists()
        }
    }

    @Test
    fun `import adds new scenarios to repository`() {
        val tempFile = createTempFile(suffix = ".json")
        try {
            service.exportToFile(tempFile)

            // Create a fresh repository with no data and import
            val emptyRepo = InMemoryScenarioRepository(emptyList())
            val importService = ScenarioJsonService(emptyRepo)
            val importedCount = importService.importFromFile(tempFile)

            assertEquals(2, importedCount)
            assertEquals(2, emptyRepo.findAll().size)
            assertNotNull(emptyRepo.findById("sc-1"))
            assertNotNull(emptyRepo.findById("sc-2"))
        } finally {
            tempFile.deleteIfExists()
        }
    }

    @Test
    fun `import skips scenarios with duplicate IDs`() {
        val tempFile = createTempFile(suffix = ".json")
        try {
            service.exportToFile(tempFile)

            // Import into the same repository that already has the data
            val importedCount = service.importFromFile(tempFile)

            assertEquals(0, importedCount)
            assertEquals(2, repository.findAll().size)
        } finally {
            tempFile.deleteIfExists()
        }
    }

    @Test
    fun `export then import roundtrip preserves data`() {
        val tempFile = createTempFile(suffix = ".json")
        try {
            service.exportToFile(tempFile)

            val emptyRepo = InMemoryScenarioRepository(emptyList())
            val importService = ScenarioJsonService(emptyRepo)
            importService.importFromFile(tempFile)

            val imported = emptyRepo.findAll()
            assertEquals(sampleScenarios.size, imported.size)

            val loginScenario = emptyRepo.findById("sc-1")!!
            assertEquals("Login with valid credentials", loginScenario.title)
            assertEquals(ScenarioType.SMOKE, loginScenario.type)
            assertEquals(ScenarioPriority.CRITICAL, loginScenario.priority)
            assertEquals(2, loginScenario.steps.size)
            assertEquals("auth", loginScenario.tags.first().name)
        } finally {
            tempFile.deleteIfExists()
        }
    }

    @Test
    fun `import fails gracefully on invalid JSON`() {
        val tempFile = createTempFile(suffix = ".json")
        try {
            tempFile.toFile().writeText("not valid json")
            assertFailsWith<Exception> {
                service.importFromFile(tempFile)
            }
        } finally {
            tempFile.deleteIfExists()
        }
    }
}
