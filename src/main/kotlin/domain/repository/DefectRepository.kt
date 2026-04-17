package domain.repository

import domain.model.Defect

interface DefectRepository {
    fun findAll(): List<Defect>
    fun findById(id: String): Defect?
    fun findByIds(ids: List<String>): List<Defect>
}
