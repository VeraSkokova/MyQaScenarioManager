package domain.repository

import domain.model.Scenario
import domain.model.ScenarioType

interface ScenarioRepository {
    fun findAll(): List<Scenario>
    fun findById(id: String): Scenario?
    fun findScenarios(query: String = "", smokeOnly: Boolean = false): List<Scenario>
    fun countByType(type: ScenarioType): Int
    fun addAll(scenarios: List<Scenario>)
}
