package domain.model

data class ScenarioStep(
    val id: String,
    val orderIndex: Int,
    val action: String,
    val expectedResult: String,
)
