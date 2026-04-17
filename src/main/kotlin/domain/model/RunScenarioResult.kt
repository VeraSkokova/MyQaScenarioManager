package domain.model

import kotlinx.datetime.Instant

data class RunScenarioResult(
    val id: String,
    val runId: String,
    val scenarioId: String,
    val status: ResultStatus,
    val comment: String = "",
    val executedAt: Instant? = null,
    val defectIds: List<String> = emptyList(),
)
