package features.scenarios.details

import domain.model.DefectStatus
import domain.model.ScenarioPriority
import domain.model.ScenarioType

data class ScenarioDetailUi(
    val id: String,
    val title: String,
    val description: String,
    val type: ScenarioType,
    val priority: ScenarioPriority,
    val tagNames: List<String>,
    val steps: List<StepUi>,
    val expectedResult: String,
)

data class StepUi(
    val orderIndex: Int,
    val action: String,
    val expectedResult: String,
)

data class DefectUi(
    val id: String,
    val title: String,
    val url: String,
    val status: DefectStatus,
)
