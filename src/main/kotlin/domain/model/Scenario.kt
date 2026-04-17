package domain.model

data class Scenario(
    val id: String,
    val title: String,
    val description: String,
    val type: ScenarioType,
    val priority: ScenarioPriority,
    val tags: List<Tag>,
    val steps: List<ScenarioStep>,
    val expectedResult: String,
    val linkedDefectIds: List<String> = emptyList(),
)
