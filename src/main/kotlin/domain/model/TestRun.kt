package domain.model

import kotlinx.datetime.Instant

data class TestRun(
    val id: String,
    val name: String,
    val environmentId: String,
    val buildInfoId: String,
    val status: RunStatus,
    val createdAt: Instant,
    val completedAt: Instant? = null,
    val scenarioIds: List<String>,
)
