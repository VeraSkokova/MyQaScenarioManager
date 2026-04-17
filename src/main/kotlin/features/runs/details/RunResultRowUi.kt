package features.runs.details

import domain.model.ResultStatus

data class RunResultRowUi(
    val resultId: String,
    val scenarioTitle: String,
    val status: ResultStatus,
    val comment: String,
    val executedAt: String?,
)
