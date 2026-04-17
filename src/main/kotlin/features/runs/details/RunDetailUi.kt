package features.runs.details

import domain.model.RunStatus

data class RunDetailUi(
    val id: String,
    val name: String,
    val environmentName: String,
    val buildVersion: String,
    val buildBranch: String,
    val status: RunStatus,
    val createdAt: String,
    val completedAt: String?,
)
