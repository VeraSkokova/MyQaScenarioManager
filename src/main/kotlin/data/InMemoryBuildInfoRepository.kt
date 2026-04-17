package data

import domain.model.BuildInfo
import domain.repository.BuildInfoRepository

class InMemoryBuildInfoRepository(
    private val buildInfos: List<BuildInfo>,
) : BuildInfoRepository {

    override fun findById(id: String): BuildInfo? = buildInfos.find { it.id == id }
}
