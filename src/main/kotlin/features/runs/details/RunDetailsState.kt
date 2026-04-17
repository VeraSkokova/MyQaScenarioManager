package features.runs.details

data class RunDetailsState(
    val run: RunDetailUi? = null,
    val results: List<RunResultRowUi> = emptyList(),
)
