package features.dashboard

data class RecentRunUi(
    val id: String,
    val name: String,
    val environmentName: String,
    val status: domain.model.RunStatus,
    val scenarioCount: Int,
    val createdAt: String,
)

data class DashboardState(
    val passRatePercent: Float = 0f,
    val failedTodayCount: Int = 0,
    val smokeScenariosTotal: Int = 0,
    val totalScenarios: Int = 0,
    val recentRuns: List<RecentRunUi> = emptyList(),
)
