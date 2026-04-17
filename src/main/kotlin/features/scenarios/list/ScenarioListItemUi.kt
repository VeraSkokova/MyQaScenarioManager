package features.scenarios.list

import domain.model.ScenarioPriority
import domain.model.ScenarioType

data class ScenarioListItemUi(
    val id: String,
    val title: String,
    val type: ScenarioType,
    val priority: ScenarioPriority,
    val tagNames: List<String>,
    val stepCount: Int,
)
