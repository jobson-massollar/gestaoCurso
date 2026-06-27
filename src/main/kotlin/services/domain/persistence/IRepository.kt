package services.domain.persistence

import model.Entity
import model.Repository
import kotlin.reflect.KClass

interface IRepository<T: Entity> {
//    fun save(e: T)
//    fun remove(e: T)
//    fun findAll(): List<T>
//    fun deleteAll()
//    fun startBatchInsert(chunkSize: Int)
//    fun batchInsert(e: T)
//    fun endBatchInsert()
}

interface IRepositoryFactory {
    fun <T: Repository<*, *, *>> get(c: KClass<in T>): T
}

