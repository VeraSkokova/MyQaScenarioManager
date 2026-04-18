package domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ScenarioPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL,
}
