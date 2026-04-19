package features.scenarios.list

import domain.model.ScenarioPriority
import domain.model.Tag

enum class ScenarioSortOption {
    TITLE_ASC,
    TITLE_DESC,
    PRIORITY_ASC,
    PRIORITY_DESC,
}

data class ScenarioListState(
    val searchQuery: String = "",
    val smokeOnly: Boolean = false,
    val priorityFilter: ScenarioPriority? = null,
    val selectedTagIds: Set<String> = emptySet(),
    val availableTags: List<Tag> = emptyList(),
    val sortOption: ScenarioSortOption = ScenarioSortOption.TITLE_ASC,
    val scenarios: List<ScenarioListItemUi> = emptyList(),
    val totalCount: Int = 0,
)
