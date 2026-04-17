package features.runs.list

import domain.model.RunStatus

data class RunListItemUi(
    val id: String,
    val name: String,
    val environmentName: String,
    val status: RunStatus,
    val scenarioCount: Int,
    val createdAt: String,
)
