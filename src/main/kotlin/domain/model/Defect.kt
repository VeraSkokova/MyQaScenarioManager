package domain.model

data class Defect(
    val id: String,
    val title: String,
    val url: String,
    val status: DefectStatus,
)
