package domain.repository

import domain.model.Environment

interface EnvironmentRepository {
    fun findAll(): List<Environment>
    fun findById(id: String): Environment?
}
