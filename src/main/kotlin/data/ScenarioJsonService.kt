package data

import domain.model.Scenario
import domain.repository.ScenarioRepository
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

class ScenarioJsonService(
    private val scenarioRepository: ScenarioRepository,
) {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    fun exportToFile(path: Path) {
        val scenarios = scenarioRepository.findAll()
        val content = json.encodeToString(scenarios)
        path.writeText(content)
    }

    fun importFromFile(path: Path): Int {
        val content = path.readText()
        val scenarios = json.decodeFromString<List<Scenario>>(content)
        val countBefore = scenarioRepository.findAll().size
        scenarioRepository.addAll(scenarios)
        val countAfter = scenarioRepository.findAll().size
        return countAfter - countBefore
    }
}
