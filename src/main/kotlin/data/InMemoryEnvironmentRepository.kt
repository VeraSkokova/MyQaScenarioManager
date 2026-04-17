package data

import domain.model.Environment
import domain.repository.EnvironmentRepository

class InMemoryEnvironmentRepository(
    private val environments: List<Environment>,
) : EnvironmentRepository {

    override fun findAll(): List<Environment> = environments

    override fun findById(id: String): Environment? = environments.find { it.id == id }
}
