package domain.repository

import domain.model.BuildInfo

interface BuildInfoRepository {
    fun findById(id: String): BuildInfo?
}
