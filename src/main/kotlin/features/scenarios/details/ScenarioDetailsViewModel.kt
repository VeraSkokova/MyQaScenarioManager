package features.scenarios.details

import domain.repository.DefectRepository
import domain.repository.ScenarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScenarioDetailsViewModel(
    private val scenarioRepository: ScenarioRepository,
    private val defectRepository: DefectRepository,
    private val scenarioId: String,
) {
    private val _state = MutableStateFlow(ScenarioDetailsState())
    val state: StateFlow<ScenarioDetailsState> = _state.asStateFlow()

    init {
        load()
    }

    private fun load() {
        val scenario = scenarioRepository.findById(scenarioId) ?: return
        val defects = defectRepository.findByIds(scenario.linkedDefectIds)

        _state.update {
            ScenarioDetailsState(
                scenario = ScenarioDetailUi(
                    id = scenario.id,
                    title = scenario.title,
                    description = scenario.description,
                    type = scenario.type,
                    priority = scenario.priority,
                    tagNames = scenario.tags.map { tag -> tag.name },
                    steps = scenario.steps.sortedBy { step -> step.orderIndex }.map { step ->
                        StepUi(
                            orderIndex = step.orderIndex,
                            action = step.action,
                            expectedResult = step.expectedResult,
                        )
                    },
                    expectedResult = scenario.expectedResult,
                ),
                linkedDefects = defects.map { defect ->
                    DefectUi(
                        id = defect.id,
                        title = defect.title,
                        url = defect.url,
                        status = defect.status,
                    )
                },
            )
        }
    }
}
