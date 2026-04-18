package features.scenarios.list

data class ScenarioListState(
    val searchQuery: String = "",
    val smokeOnly: Boolean = false,
    val scenarios: List<ScenarioListItemUi> = emptyList(),
    val userMessage: String? = null,
)
