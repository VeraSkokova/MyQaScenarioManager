package data

import domain.model.Scenario
import domain.model.ScenarioType
import domain.repository.ScenarioRepository

class InMemoryScenarioRepository(
    private val scenarios: List<Scenario>,
) : ScenarioRepository {

    override fun findAll(): List<Scenario> = scenarios

    override fun findById(id: String): Scenario? = scenarios.find { it.id == id }

    override fun findScenarios(query: String, smokeOnly: Boolean): List<Scenario> {
        val lowerQuery = query.lowercase()
        return scenarios.filter { scenario ->
            val matchesQuery = lowerQuery.isBlank() ||
                scenario.title.lowercase().contains(lowerQuery) ||
                scenario.description.lowercase().contains(lowerQuery)
            val matchesType = !smokeOnly || scenario.type == ScenarioType.SMOKE
            matchesQuery && matchesType
        }
    }

    override fun countByType(type: ScenarioType): Int = scenarios.count { it.type == type }
}
