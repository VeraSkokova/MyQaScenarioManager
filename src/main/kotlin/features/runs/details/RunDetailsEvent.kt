package features.runs.details

import domain.model.ResultStatus

sealed interface RunDetailsEvent {
    data object BackClicked : RunDetailsEvent
    data class StatusChanged(val resultId: String, val newStatus: ResultStatus) : RunDetailsEvent
}
