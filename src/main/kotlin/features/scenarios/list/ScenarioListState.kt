package features.scenarios.list

import domain.model.ScenarioPriority
import domain.model.Tag

data class ScenarioListState(
    val searchQuery: String = "",
    val smokeOnly: Boolean = false,
    val priorityFilter: ScenarioPriority? = null,
    val selectedTagIds: Set<String> = emptySet(),
    val availableTags: List<Tag> = emptyList(),
    val scenarios: List<ScenarioListItemUi> = emptyList(),
)
