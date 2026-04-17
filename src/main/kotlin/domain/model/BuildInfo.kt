package domain.model

data class BuildInfo(
    val id: String,
    val version: String,
    val branch: String,
    val commitHash: String,
)
