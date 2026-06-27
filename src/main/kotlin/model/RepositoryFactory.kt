package model

import services.domain.persistence.IRepositoryFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

object RepositoryFactory : IRepositoryFactory {

    private val repositories = HashMap<String, Repository<*, *, *>>()

    override fun <T: Repository<*, *, *>> get(c: KClass<in T>): T {
        if (! repositories.containsKey(c.java.name))
            repositories[c.java.name] = c.createInstance() as T
        return repositories[c.java.name] as T
    }
}