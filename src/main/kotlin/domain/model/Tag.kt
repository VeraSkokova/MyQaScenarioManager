package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val id: String,
    val name: String,
)
