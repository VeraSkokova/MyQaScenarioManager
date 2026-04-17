package data

import domain.model.Defect
import domain.repository.DefectRepository

class InMemoryDefectRepository(
    private val defects: List<Defect>,
) : DefectRepository {

    override fun findAll(): List<Defect> = defects

    override fun findById(id: String): Defect? = defects.find { it.id == id }

    override fun findByIds(ids: List<String>): List<Defect> {
        val idSet = ids.toSet()
        return defects.filter { it.id in idSet }
    }
}
