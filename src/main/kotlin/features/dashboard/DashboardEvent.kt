package features.dashboard

sealed interface DashboardEvent {
    data class RunSelected(val runId: String) : DashboardEvent
}
