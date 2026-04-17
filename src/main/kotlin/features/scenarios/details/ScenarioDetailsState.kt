package features.scenarios.details

data class ScenarioDetailsState(
    val scenario: ScenarioDetailUi? = null,
    val linkedDefects: List<DefectUi> = emptyList(),
)
