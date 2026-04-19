package features.scenarios.list

import domain.model.ScenarioPriority

data class ScenarioListState(
    val searchQuery: String = "",
    val smokeOnly: Boolean = false,
    val priorityFilter: ScenarioPriority? = null,
    val scenarios: List<ScenarioListItemUi> = emptyList(),
)
