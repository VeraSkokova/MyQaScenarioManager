package domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ScenarioType {
    SMOKE,
    FUNCTIONAL,
}
