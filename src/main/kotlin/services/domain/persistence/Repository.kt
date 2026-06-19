package services.domain.persistence

import model.Entity
import model.EntityDTO
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

interface IRepository<T: Entity> {
//    fun save(e: T)
//    fun remove(e: T)
//    fun findAll(): List<T>
//    fun deleteAll()
//    fun startBatchInsert(chunkSize: Int)
//    fun batchInsert(e: T)
//    fun endBatchInsert()
}

abstract class Repository<T: Entity, S: IDAO<T, V>, V: EntityDTO<T>>: IRepository<T> {

//    private var chunkSize: Int = 0
//    private var chunk: MutableList<V>? = null

    abstract val dao: S

    fun createEntity(dto: V): T {
        val c = dto.toEntity()
        c.id = dto.id
        return c
    }

//    override fun save(e: T) {
//        if (e.id == null) {
//            e.id = Uuid.random()
//            dao.insert(createDTO(e))
//        }
//        else
//            dao.update(createDTO(e))
//    }

//    override fun remove(e: T) {
//        if (e.id != null) {
//            dao.delete(createDTO(e))
//            e.id = null
//        }
//    }

//    override fun findAll(): List<T> =
//        toEntityList(dao.findAll())

    protected fun createEntityList(dtos: List<V>): List<T> = dtos.map {
        it.toEntity()
    }.toList()


//    override fun deleteAll() {
//        dao.deleteAll()
//    }

//    override fun startBatchInsert(chunkSize: Int) {
//        if (chunkSize > 0) {
//            this.chunkSize = chunkSize
//            chunk = mutableListOf()
//        }
//    }

//    override fun batchInsert(e: T) {
//        chunk?.let {
//            if (e.id == null)
//                e.id = Uuid.random()
//            it.add(createDTO(e))
//            if (it.size == chunkSize) {
//                dao.batchInsert(it)
//                it.clear()
//            }
//        }
//    }

//    override fun endBatchInsert() {
//        chunk?.let {
//            if (it.size > 0) {
//                dao.batchInsert(it)
//                it.clear()
//            }
//        }
//        chunk = null
//    }
}

interface IRepositoryFactory {
    fun <T:Repository<*,*,*>> get(c: KClass<in T>): T
}

object RepositoryFactory : IRepositoryFactory {

    private val repositories = HashMap<String, Repository<*, *, *>>()

    override fun <T:Repository<*,*,*>> get(c: KClass<in T>): T {
        if (! repositories.containsKey(c.java.name))
            repositories[c.java.name] = c.createInstance() as T
        return repositories[c.java.name] as T
    }
}