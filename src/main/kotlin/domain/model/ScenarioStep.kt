package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ScenarioStep(
    val id: String,
    val orderIndex: Int,
    val action: String,
    val expectedResult: String,
)
