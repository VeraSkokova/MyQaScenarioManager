package features.scenarios.details

sealed interface ScenarioDetailsEvent {
    data object BackClicked : ScenarioDetailsEvent
}
