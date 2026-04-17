package app

sealed class Route {
    data object Dashboard : Route()
    data object ScenarioList : Route()
    data class ScenarioDetails(val scenarioId: String) : Route()
    data object RunList : Route()
    data class RunDetails(val runId: String) : Route()
    data object Settings : Route()
}
