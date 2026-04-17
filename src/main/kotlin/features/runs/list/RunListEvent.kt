package features.runs.list

sealed interface RunListEvent {
    data class RunSelected(val runId: String) : RunListEvent
}
