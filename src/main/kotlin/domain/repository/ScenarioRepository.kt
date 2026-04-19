package domain.repository

import domain.model.Scenario
import domain.model.ScenarioPriority
import domain.model.ScenarioType

interface ScenarioRepository {
    fun findAll(): List<Scenario>
    fun findById(id: String): Scenario?
    fun findScenarios(
        query: String = "",
        smokeOnly: Boolean = false,
        priority: ScenarioPriority? = null,
        tagIds: Set<String> = emptySet(),
    ): List<Scenario>
    fun countByType(type: ScenarioType): Int
}
