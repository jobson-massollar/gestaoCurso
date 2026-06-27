package model

import services.domain.persistence.IDAO
import services.domain.persistence.IRepository

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