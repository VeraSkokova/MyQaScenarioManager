// src/main/kotlin/domain/repository/ScenarioRepository.kt

package domain.repository

import domain.model.Scenario
import domain.model.ScenarioType
import java.io.FileWriter
import java.io.PrintWriter
import org.json.JSONArray

interface ScenarioRepository {
    fun findAll(): List<Scenario>
    fun findById(id: String): Scenario?
    fun findScenarios(query: String = "", smokeOnly: Boolean = false): List<Scenario>
    fun countByType(type: ScenarioType): Int

    /**
     * Saves a list of scenarios to JSON file.
     */
    fun saveToJson(file: String, scenarios: List<Scenario>) {
        val json = JSONArray(scenarios)
        val fw = FileWriter(file)
        val pw = PrintWriter(fw)
        pw.println(json.toString())
        pw.close()
    }

    /**
     * Loads a list of scenarios from JSON file.
     */
    fun loadFromJson(file: String): List<Scenario> {
        // Add the implementation for loading scenarios from a JSON file
    }
}